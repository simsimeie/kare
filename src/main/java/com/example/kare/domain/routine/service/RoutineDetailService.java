package com.example.kare.domain.routine.service;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.routine.dto.CreateRoutineAchieveDetailReqDto;
import com.example.kare.domain.routine.dto.CreateRoutineAchieveReqDto;
import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.entity.routine.MmrRoutnMgt;
import com.example.kare.entity.routine.constant.AchieveStatus;
import com.example.kare.entity.routine.id.MmrRoutnAhvHisId;
import com.example.kare.entity.routine.id.MmrRoutnMgtId;
import com.example.kare.repository.MmrRoutnAchHisRepo;
import com.example.kare.repository.MmrRoutnDtlMgtRepo;
import com.example.kare.repository.MmrRoutnMgtRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineDetailService {
    private final MmrRoutnMgtRepo mmrRoutnMgtRepo;
    private final MmrRoutnDtlMgtRepo mmrRoutnDtlMgtRepo;
    private final MmrRoutnAchHisRepo mmrRoutnAchHisRepo;

    @Transactional
    public void inputRoutineAchievement(CreateRoutineAchieveReqDto createRoutineAchieveReqDto) {
        for (CreateRoutineAchieveDetailReqDto reqDto : createRoutineAchieveReqDto.getRoutineList()) {
            Optional<MmrRoutnDtlMgt> routineDetailResult = mmrRoutnDtlMgtRepo.findValidRoutineDetail(
                    reqDto.getRoutineSequence(),
                    reqDto.getMemberId(),
                    reqDto.getRequestDate()
            );

            if(routineDetailResult.isEmpty()){
                throw new KBException("루틴 시작일 이후의 루틴에 대해서만 완료가 가능합니다.", ErrorCode.BAD_REQUEST);
            }
            MmrRoutnDtlMgt routineDetail = routineDetailResult.get();
            validationCheck(routineDetail, reqDto);

            Optional<MmrRoutnAhvHis> routineHistoryResult = findRoutineAchievement(routineDetail, reqDto.getRequestDate());
            // 배경 : 달성치 변경은 오직 값만 변경이 가능하다. 목표유형과 목표단위는 변경할 수 없다.
            if (routineHistoryResult.isPresent()) {
                MmrRoutnAhvHis routineAchievement = routineHistoryResult.get();
                updateRoutineAchievement(reqDto, routineDetail, routineAchievement);
            } else {
                MmrRoutnAhvHis routineAchievement = createRoutineAchievement(routineDetail, reqDto);
                mmrRoutnAchHisRepo.save(routineAchievement);
            }
        }
    }

    private Optional<MmrRoutnAhvHis> findRoutineAchievement(MmrRoutnDtlMgt routineDetail, LocalDate requestDate) {
        MmrRoutnAhvHisId id = new MmrRoutnAhvHisId(
                requestDate,
                routineDetail.getRoutnChDt(),
                routineDetail.getRoutnSeq(),
                routineDetail.getMmrId()
        );
        return mmrRoutnAchHisRepo.findById(id);
    }

    private void updateRoutineAchievement(CreateRoutineAchieveDetailReqDto reqDto, MmrRoutnDtlMgt routineDetail, MmrRoutnAhvHis achievement) {
        if (routineDetail.getGoal().getGolTpCd().equals(1)) {
            achievement.setGoalAchievementStatus(AchieveStatus.Y);
        } else {
            if (reqDto.getGoal().getGoalValue() >= routineDetail.getGoal().getGolVal()) {
                achievement.setGoalAchievementStatus(AchieveStatus.Y);
            } else {
                achievement.setGoalAchievementStatus(AchieveStatus.N);
            }
        }

        achievement.changeGoalValue(reqDto.getGoal());
    }

    private MmrRoutnAhvHis createRoutineAchievement(MmrRoutnDtlMgt routineDetail, CreateRoutineAchieveDetailReqDto achievementDto) {
        AchieveStatus status = AchieveStatus.N;

        validationCheck(routineDetail, achievementDto);

        if (routineDetail.getGoal().getGolTpCd().equals(1)) {
            status = AchieveStatus.Y;
        } else if (routineDetail.getGoal().getGolVal() <= achievementDto.getGoal().getGoalValue()) {
            status = AchieveStatus.Y;
        }

        return MmrRoutnAhvHis.createRoutineAchievement(
                achievementDto.getRequestDate(),
                routineDetail.getRoutnChDt(),
                routineDetail.getRoutnSeq(),
                routineDetail.getMmrId(),
                achievementDto.getGoal().toEntity(),
                status
        );
    }

    public void validationCheck(MmrRoutnDtlMgt routineDetail, CreateRoutineAchieveDetailReqDto reqDto) {
        if (routineDetail.getGoal().getGolTpCd().equals(1)) {
            if (reqDto.getGoal().getGoalUnitTypeCode() != null
                    || reqDto.getGoal().getGoalValue() != null
                    || !reqDto.getGoal().getGoalTypeCode().equals(1)) {
                throw new KBException("설정하신 목표유형,단위로만 입력하실 수 있습니다.", ErrorCode.BAD_REQUEST);
            }
        } else {
            if (!routineDetail.getGoal().getGolUnitTpCd().equals(reqDto.getGoal().getGoalUnitTypeCode())
                    || !routineDetail.getGoal().getGolTpCd().equals(reqDto.getGoal().getGoalTypeCode())) {
                throw new KBException("설정하신 목표유형,단위로만 입력하실 수 있습니다.", ErrorCode.BAD_REQUEST);
            }
        }

        if (Period.between(routineDetail.getStartDate(), reqDto.getRequestDate()).isNegative()){
            throw new KBException("루틴 시작일 이후의 루틴에 대해서만 완료가 가능합니다.", ErrorCode.BAD_REQUEST);
        }
    }

    @Transactional
    public void deleteRoutine(Integer routnSeq, String memberId) {
        Optional<MmrRoutnMgt> routine = mmrRoutnMgtRepo.findById(new MmrRoutnMgtId(routnSeq, memberId));
        if (routine.isPresent()) {
            mmrRoutnMgtRepo.delete(routine.get());
        }
    }
}
