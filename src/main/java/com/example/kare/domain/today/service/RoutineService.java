package com.example.kare.domain.today.service;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.*;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.*;
import com.example.kare.repository.MemberRepository;
import com.example.kare.repository.RoutineGroupRepo;
import com.example.kare.repository.RoutineDetailRepo;
import com.example.kare.repository.RoutineRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoutineService {
    private final MemberService memberService;
    private final RoutineGroupService routineGroupService;
    private final CommonService commonService;
    private final RoutineRepo routineRepo;
    private final RoutineDetailRepo routineDetailRepo;
    private final EntityManager entityManager;
    @Value("${active.routine.limit}")
    private Integer limit;


    @Transactional
    public Integer inputRoutine(CreateRoutineRequestDto reqDto) {
        // TODO : reqDto.getRoutines().get(0) 부분 memberId AccessToken으로 받을 경우 로직 변경
        String mmrId = reqDto.getRoutineList().get(0).getMemberId();

        validationCheck(mmrId, reqDto.getRoutineList().size());

        for (RoutineRequestDto routineDto : reqDto.getRoutineList()) {
            Routine routine = createRoutine(routineDto);
            routineRepo.save(routine);
            entityManager.flush();

            if (routineDto.getRoutineGroupSequence() != null) {
                RoutineGroup routineGroup = routineGroupService.findRoutineGroup(routineDto.getRoutineGroupSequence(), routine.getMember().getId());
                routine.mapToRoutineGroup(routineGroup);
            } else if (routineDto.getRoutineGroupName() != null) {
                RoutineGroupId routineGroupId = routineGroupService.inputRoutineGroup(mmrId, routineDto.getRoutineGroupName());
                routine.mapToRoutineGroup(routineGroupId);
            }
            entityManager.flush();
        }

        return reqDto.getRoutineList().size();
    }

    protected void validationCheck(String mmrId, Integer routineSizeToAdd) {
        Integer activeRoutineNum = routineRepo.findActiveRoutineNum(mmrId);
        if (limit < activeRoutineNum + routineSizeToAdd) {
            throw new KBException(limit + "개 이하의 루틴만 등록 가능합니다.", ErrorCode.BAD_REQUEST);
        }
    }

    private Routine createRoutine(RoutineRequestDto routineDto) {
        Member member = memberService.findMember(routineDto.getMemberId());
        Integer routineSeq = routineRepo.findMaxRoutnSeq(member);
        Integer sortOrder = commonService.findMinSoOrd(member);

        return createRoutine(routineDto, member, routineSeq, sortOrder);
    }

    private Routine createRoutine(RoutineRequestDto routineDto,
                                  Member member,
                                  Integer routineSequence,
                                  Integer sortOrder) {

        return routineDto.toEntity(member, routineSequence, sortOrder);
    }

    public Map<String, Object> retrieveRoutine(String memberId, LocalDate searchDate) {
        List<RoutineResponseDto> routines = new ArrayList<>();
        Set<RoutineGroupResponseDto> routineGroups = new HashSet<>();
        Map<Integer, List<RoutineResponseDto>> mapByRoutineGroup = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        // 오늘
        if (Period.between(searchDate, LocalDate.now()).isZero()) {
            return null;
        }
        // 미래
        else if (Period.between(searchDate, LocalDate.now()).isNegative()) {
            List<RoutineResponseDto> futureRoutines = routineRepo.findFutureRoutines(memberId, searchDate);
            futureRoutines.forEach(routine -> {
                if (null != routine.getRoutineGroupSequence()) {
                    routineGroups.add(new RoutineGroupResponseDto(routine.getRoutineGroupSequence(), routine.getRoutineGroupName()));
                    List<RoutineResponseDto> list = Optional.ofNullable(mapByRoutineGroup.get(routine.getRoutineGroupSequence())).orElseGet(ArrayList::new);
                    list.add(routine);
                    mapByRoutineGroup.put(routine.getRoutineGroupSequence(), list);
                } else {
                    routines.add(routine);
                }
            });

            for (RoutineGroupResponseDto group : routineGroups) {
                List<RoutineResponseDto> routinesInGroup = mapByRoutineGroup.get(group.getRoutineGroupSequence());
                group.setRoutines(routinesInGroup);
                group.setTotalCount(routinesInGroup.size());
            }

            result.put("routineList", routines);
            result.put("routineGroupList", routineGroups);

            return result;
        }
        // 과거
        else {
            return null;
        }
    }

    @Transactional
    public RoutineId modifyRoutine(RoutineRequestDto routineRequestDto) {
        Routine routine = findRoutine(
                routineRequestDto.getRoutineSequence(),
                routineRequestDto.getMemberId()
        );
        routine.modifyRoutine(routineRequestDto);

        RoutineDetail routineDetail = findActiveRoutineDetail(routine);

        if (routineDetail.isShouldUpdateRoutineHistory(routine)) {
            // start date가 변경된 경우에는 현재 < 변경할 start date && Last History start date 경우만 고려하면 된다.
            // 즉, start date가 변경되었다는 것은 아직 시작되지 않은 미래의 루틴을 변경했다는 의미로 history 테이블엔 row 1줄만 존재
            // 따라서, start date가 변경된 경우에는 업데이트만 해주면 된다.
            if (!routineDetail.getStartDate().equals(routine.getStartDate())) {
                routineDetail.modifyRoutineCharacter(routine);
                routineDetail.modifyRoutineStartDate(routine.getStartDate());
                //routineHistoryRepository.save(routineHistory);
            }
            // 현재 <= Last History의 start date < 변경할 start date : history insert
            else {
                // 오늘 생성해서 오늘 주기나 목표를 변경한 경우에는 history 업데이트
                if (Period.between(routineDetail.getRoutnChDt(), LocalDate.now()).isZero()) {
                    routineDetail.modifyRoutineCharacter(routine);
                    routineDetail.modifyRoutineStartDate(LocalDate.now());
                    routineDetailRepo.save(routineDetail);
                }
                // 그 외는 insert
                else {
                    routineDetail.modifyRoutineHistoryEndDate(LocalDate.now().minusDays(1));
                    routineDetailRepo.save(routineDetail);
                    RoutineDetail newRoutineHistory = RoutineDetail.createRoutineHistory(routine, LocalDate.now());
                    routineDetailRepo.save(newRoutineHistory);
                }
            }
        }
        return routine.getId();
    }

    private RoutineDetail findActiveRoutineDetail(Routine routine) {
        RoutineDetail active = routineDetailRepo.findActiveRoutineDetail(
                routine.getRoutnSeq(),
                routine.getMember().getId()
        );
        return active;
    }

    private Routine findRoutine(Integer routnId, String memberId) {
        Optional<Routine> routine = routineRepo.findById(new RoutineId(routnId, memberId));
        if (routine.isEmpty()) {
            throw new KBException("존재하지 않는 루틴입니다.", ErrorCode.BAD_REQUEST);
        }
        return routine.get();
    }


}
