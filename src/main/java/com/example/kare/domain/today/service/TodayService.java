package com.example.kare.domain.today.service;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.*;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import com.example.kare.entity.routine.RoutineGroup;
import com.example.kare.entity.routine.RoutineHistory;
import com.example.kare.repository.MemberRepository;
import com.example.kare.repository.RoutineGroupRepository;
import com.example.kare.repository.RoutineHistoryRepository;
import com.example.kare.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayService {
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final RoutineGroupRepository routineGroupRepository;
    private final RoutineHistoryRepository routineHistoryRepository;


    public Map<String, Object> retrieveRoutine(String memberId, LocalDate searchDate){
        List<RoutineResponseDto> routines = new ArrayList<>();
        Set<RoutineGroupResponseDto> routineGroups = new HashSet<>();
        Map<Long, List<RoutineResponseDto>> mapByRoutineGroup = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        // 오늘
        if(Period.between(searchDate, LocalDate.now()).isZero()){
            return null;
        }
        // 미래
        else if(Period.between(searchDate, LocalDate.now()).isNegative()){
            List<RoutineResponseDto> futureRoutines = routineRepository.findFutureRoutines(memberId, searchDate);
            futureRoutines.forEach(routine -> {
                if(null != routine.getRoutineGroupId()){
                    routineGroups.add(new RoutineGroupResponseDto(routine.getRoutineGroupId(), routine.getRoutineGroupName()));
                    List<RoutineResponseDto> list = Optional.ofNullable(mapByRoutineGroup.get(routine.getRoutineGroupId())).orElseGet(ArrayList::new);
                    list.add(routine);
                    mapByRoutineGroup.put(routine.getRoutineGroupId(), list);
                }else{
                    routines.add(routine);
                }
            });

            for(RoutineGroupResponseDto group : routineGroups){
                List<RoutineResponseDto> routinesInGroup = mapByRoutineGroup.get(group.getRoutineGroupId());
                group.setRoutines(routinesInGroup);
                group.setTotalCount(routinesInGroup.size());
            }

            result.put("Routine", routines);
            result.put("RoutineGroup", routineGroups);

            return result;
        }
        // 과거
        else{
            return null;
        }
    }
    @Transactional
    public Long createRoutine(RoutineRequestDto routineRequestDto){
        Routine routine = transformRoutineRequestDtoToRoutine(routineRequestDto);
        routineRepository.save(routine);
        return routine.getId();
    }

    protected Routine transformRoutineRequestDtoToRoutine(RoutineRequestDto routineRequestDto){
        Member member = getMember(routineRequestDto.getMemberId());
        Integer routineDisplayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);
        return routineRequestDto.toEntity(member, routineDisplayLeastValue);
    }

    private Member getMember(String memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()){
            throw new KBException("존재하지 않는 회원입니다.", ErrorCode.BAD_REQUEST);
        }
        return member.get();
    }

    @Transactional
    public Long modifyRoutine(RoutineRequestDto routineRequestDto){
        Routine routine = getRoutine(routineRequestDto.getRoutineId());
        Routine modifiedRoutine = routine.modifyRoutine(routineRequestDto);
        routineRepository.save(modifiedRoutine);

        RoutineHistory routineHistory = getLastRoutineHistory(routine);

        if(routineHistory.isShouldUpdateRoutineHistory(modifiedRoutine)){
            // start date가 변경된 경우에는 현재 < 변경할 start date && Last History start date 경우만 고려하면 된다.
            // 즉, start date가 변경되었다는 것은 아직 시작되지 않은 미래의 루틴을 변경했다는 의미로 history 테이블엔 row 1줄만 존재
            // 따라서, start date가 변경된 경우에는 업데이트만 해주면 된다.
            if(! routine.getStartDate().equals(modifiedRoutine.getStartDate())){
                routineHistory.modifyRoutineCharacter(modifiedRoutine);
                routineHistory.modifyRoutineStartDate(modifiedRoutine.getStartDate());
                routineHistoryRepository.save(routineHistory);
            }
            // 현재 <= Last History의 start date < 변경할 start date : history insert
            else{
                // 오늘 생성해서 오늘 주기나 목표를 변경한 경우에는 history 업데이트
                if(Period.between(routineHistory.getStartDate(), LocalDate.now()).isZero()){
                    routineHistory.modifyRoutineCharacter(modifiedRoutine);
                    routineHistory.modifyRoutineStartDate(LocalDate.now());
                    routineHistoryRepository.save(routineHistory);
                }
                // 그 외는 insert
                else {
                    routineHistory.modifyRoutineHistoryEndDate(LocalDate.now().minusDays(1));
                    routineHistoryRepository.save(routineHistory);
                    RoutineHistory newRoutineHistory = RoutineHistory.createRoutineHistory(modifiedRoutine, LocalDate.now());
                    routineHistoryRepository.save(newRoutineHistory);
                }
            }
        }
        return modifiedRoutine.getId();
    }
    private RoutineHistory getLastRoutineHistory(Routine routine) {
        RoutineHistory last = routineHistoryRepository.findLastHistoryByRoutineAndEndDate(
                routine
                , LocalDate.of(9999, 12, 31)
        );
        return last;
    }
    @Transactional
    public Long createRoutineGroup(RoutineGroupRequestDto routineGroupRequestDto){
        RoutineGroup routineGroup = transformRoutineGroupRequestDtoToRoutineGroup(routineGroupRequestDto);
        routineGroupRepository.save(routineGroup);
        return routineGroup.getId();
    }

    protected RoutineGroup transformRoutineGroupRequestDtoToRoutineGroup(RoutineGroupRequestDto routineGroupRequestDto){
        Member member = getMember(routineGroupRequestDto.getMemberId());
        Integer routineDisplayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);
        return routineGroupRequestDto.toEntity(
                member
                ,routineGroupRequestDto.getName()
                ,routineDisplayLeastValue
        );
    }

    @Transactional
    public Long linkRoutineAndRoutineGroup(LinkRoutineGroupRequestDto linkRoutineGroupRequestDto){
        Routine routine = getRoutine(linkRoutineGroupRequestDto.getRoutineId());

        if(null != routine.getLinkRoutineGroup()){
            routine.clearRoutineGroup();
            routineRepository.save(routine);
        }

        if(null != linkRoutineGroupRequestDto.getRoutineGroupId()){
            RoutineGroup routineGroup = getRoutineGroup(linkRoutineGroupRequestDto.getRoutineGroupId());
            routine.addRoutineToGroup(routineGroup);
            routineRepository.save(routine);
        }

        return routine.getId();
    }

    private Routine getRoutine(Long routineId) {
        Optional<Routine> routine = routineRepository.findById(routineId);
        if(routine.isEmpty()){
            throw new KBException("존재하지 않는 루틴입니다.", ErrorCode.BAD_REQUEST);
        }
        return routine.get();
    }

    private RoutineGroup getRoutineGroup(Long routineGroupId){
        Optional<RoutineGroup> routineGroup = routineGroupRepository.findById(routineGroupId);
        if(routineGroup.isEmpty()){
            throw new KBException("존재하지 않는 루틴 그룹입니다.", ErrorCode.BAD_REQUEST);
        }
        return routineGroup.get();
    }




}
