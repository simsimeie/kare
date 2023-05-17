package com.example.kare.domain.routine.service;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.routine.dto.CreateRoutineDetailReqDto;
import com.example.kare.domain.routine.dto.CreateRoutineReqDto;
import com.example.kare.domain.today.dto.*;
import com.example.kare.domain.today.service.CommonService;
import com.example.kare.domain.today.service.MemberService;
import com.example.kare.domain.routinegroup.service.RoutineGroupService;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.*;
import com.example.kare.entity.routine.id.MmrRoutnGrpMgtId;
import com.example.kare.entity.routine.id.MmrRoutnMgtId;
import com.example.kare.repository.MmrRoutnDtlMgtRepo;
import com.example.kare.repository.MmrRoutnMgtRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final MmrRoutnMgtRepo mmrRoutnMgtRepo;
    private final MmrRoutnDtlMgtRepo mmrRoutnDtlMgtRepo;
    private final EntityManager entityManager;
    @Value("${active.routine.limit}")
    private Integer limit;


    @Transactional
    public Integer inputRoutine(CreateRoutineReqDto reqDto) {
        // TODO : reqDto.getRoutines().get(0) 부분 memberId AccessToken으로 받을 경우 로직 변경
        String mmrId = reqDto.getRoutineList().get(0).getMemberId();

        validationCheck(mmrId, reqDto.getRoutineList().size());

        for (CreateRoutineDetailReqDto routineDto : reqDto.getRoutineList()) {
            MmrRoutnMgt mmrRoutnMgt = createRoutine(routineDto);
            mmrRoutnMgtRepo.save(mmrRoutnMgt);
            entityManager.flush();

            if (routineDto.getRoutineGroupSequence() != null) {
                MmrRoutnGrpMgt mmrRoutnGrpMgt = routineGroupService.findRoutineGroup(routineDto.getRoutineGroupSequence(), mmrRoutnMgt.getMember().getId());
                mmrRoutnMgt.mapToRoutineGroup(mmrRoutnGrpMgt);
            } else if (routineDto.getRoutineGroupName() != null) {
                MmrRoutnGrpMgtId mmrRoutnGrpMgtId = routineGroupService.inputRoutineGroup(mmrId, routineDto.getRoutineGroupName());
                mmrRoutnMgt.mapToRoutineGroup(mmrRoutnGrpMgtId);
            }
            entityManager.flush();
        }

        return reqDto.getRoutineList().size();
    }

    protected void validationCheck(String mmrId, Integer routineSizeToAdd) {
        Integer activeRoutineNum = mmrRoutnMgtRepo.findActiveRoutnNum(mmrId);
        if (limit < activeRoutineNum + routineSizeToAdd) {
            throw new KBException(limit + "개 이하의 루틴만 등록 가능합니다.", ErrorCode.BAD_REQUEST);
        }
    }

    private MmrRoutnMgt createRoutine(CreateRoutineDetailReqDto reqDto) {
        Member member = memberService.findMember(reqDto.getMemberId());
        Integer routineSeq = mmrRoutnMgtRepo.findMaxRoutnSeq(member);
        Integer sortOrder = commonService.findMinSoOrd(member);

        return MmrRoutnMgt.createRoutine(
                reqDto.getRoutineName(),
                routineSeq,
                member,
                reqDto.getNotificationStatus(),
                reqDto.getNotificationTime(),
                reqDto.getRepeatCycle().toEntity(),
                reqDto.getGoal().toEntity(),
                reqDto.getStartDate(),
                reqDto.getEndDate(),
                sortOrder
        );
    }




    @Transactional
    public MmrRoutnMgtId modifyRoutine(ModifyRoutineReqDto reqDto) {
        MmrRoutnMgt mmrRoutnMgt = findRoutine(
                reqDto.getRoutineSequence(),
                reqDto.getMemberId()
        );
        mmrRoutnMgt.modifyRoutine(reqDto);

        MmrRoutnDtlMgt routineDetail = findActiveRoutineDetail(mmrRoutnMgt);

        if (routineDetail.isShouldUpdateRoutineHistory(mmrRoutnMgt)) {
            // start date가 변경된 경우에는 현재 < 변경할 start date && Last History start date 경우만 고려하면 된다.
            // 즉, start date가 변경되었다는 것은 아직 시작되지 않은 미래의 루틴을 변경했다는 의미로 history 테이블엔 row 1줄만 존재
            // 따라서, start date가 변경된 경우에는 업데이트만 해주면 된다.
            if (!routineDetail.getStDt().equals(mmrRoutnMgt.getStartDate())) {
                routineDetail.modifyRoutineCharacter(mmrRoutnMgt);
                routineDetail.modifyRoutineDetailStartDate(mmrRoutnMgt.getStartDate());
                //routineHistoryRepository.save(routineHistory);
            }
            // 현재 <= Last History의 start date < 변경할 start date : history insert
            else {
                // 오늘 생성해서 오늘 주기나 목표를 변경한 경우에는 history 업데이트
                if (Period.between(routineDetail.getRoutnChDt(), LocalDate.now()).isZero()) {
                    routineDetail.modifyRoutineCharacter(mmrRoutnMgt);
                    routineDetail.modifyRoutineDetailStartDate(LocalDate.now());
                    mmrRoutnDtlMgtRepo.save(routineDetail);
                }
                // 그 외는 insert
                else {
                    routineDetail.modifyRoutineDetailEndDate(LocalDate.now().minusDays(1));
                    mmrRoutnDtlMgtRepo.save(routineDetail);
                    MmrRoutnDtlMgt newRoutineHistory = MmrRoutnDtlMgt.createRoutineDetails(mmrRoutnMgt, LocalDate.now());
                    mmrRoutnDtlMgtRepo.save(newRoutineHistory);
                }
            }
        }
        return mmrRoutnMgt.getId();
    }

    private MmrRoutnDtlMgt findActiveRoutineDetail(MmrRoutnMgt mmrRoutnMgt) {
        MmrRoutnDtlMgt active = mmrRoutnDtlMgtRepo.findActiveRoutnDtl(
                mmrRoutnMgt.getRoutnSeq(),
                mmrRoutnMgt.getMember().getId()
        );
        return active;
    }

    private MmrRoutnMgt findRoutine(Integer routnId, String memberId) {
        Optional<MmrRoutnMgt> routine = mmrRoutnMgtRepo.findById(new MmrRoutnMgtId(routnId, memberId));
        if (routine.isEmpty()) {
            throw new KBException("존재하지 않는 루틴입니다.", ErrorCode.BAD_REQUEST);
        }
        return routine.get();
    }

    @Transactional
    public Integer modifyRoutineGroupSeqToNull(Integer routineGroupSeq,String memberId){
        return mmrRoutnMgtRepo.bulkUpdateRoutnGrpSeqToNull(routineGroupSeq, memberId);
    }




}
