package com.example.kare.domain.routine.service;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.routine.dto.CreateRoutineAchieveDetailReqDto;
import com.example.kare.domain.routine.dto.CreateRoutineAchieveReqDto;
import com.example.kare.domain.routine.dto.DeleteRoutineAchieveReqDto;
import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.entity.routine.constant.AchieveStatus;
import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.id.MmrRoutnAhvHisId;
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
    private final MmrRoutnDtlMgtRepo mmrRoutnDtlMgtRepo;
    private final MmrRoutnAchHisRepo mmrRoutnAchHisRepo;

    @Transactional
    public void saveRoutineAchievement(CreateRoutineAchieveReqDto createRoutineAchieveReqDto) {
        LocalDate requestDate = createRoutineAchieveReqDto.getRequestDate();
        String memberId = createRoutineAchieveReqDto.getMemberId();

        for (CreateRoutineAchieveDetailReqDto reqDto : createRoutineAchieveReqDto.getRoutineList()) {
            Optional<MmrRoutnDtlMgt> routineDetailResult = mmrRoutnDtlMgtRepo.findValidRoutnDtl(
                    reqDto.getRoutineSequence(),
                    memberId,
                    requestDate
            );
            if (routineDetailResult.isEmpty()) {
                throw new KBException("루틴 시작일 이후의 루틴에 대해서만 완료가 가능합니다.", ErrorCode.BAD_REQUEST);
            }

            MmrRoutnDtlMgt routineDetail = routineDetailResult.get();
            validationCheck(routineDetail, reqDto, requestDate);

            Optional<MmrRoutnAhvHis> routineHistoryResult = findRoutineAchievement(routineDetail, requestDate);
            // 배경 : 달성치 변경은 오직 값만 변경이 가능하다. 목표유형과 목표단위는 변경할 수 없다.
            if (routineHistoryResult.isPresent()) {
                MmrRoutnAhvHis routineAchievement = routineHistoryResult.get();
                updateRoutineAchievement(routineDetail, reqDto, routineAchievement);
            } else {
                MmrRoutnAhvHis routineAchievement = createRoutineAchievement(routineDetail, reqDto, requestDate);
                mmrRoutnAchHisRepo.save(routineAchievement);
            }
        }
    }

    public void validationCheck(MmrRoutnDtlMgt routineDetail,
                                CreateRoutineAchieveDetailReqDto reqDto,
                                LocalDate requestDate) {

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

        if (Period.between(routineDetail.getStDt(), requestDate).isNegative()) {
            throw new KBException("루틴 시작일 이후의 루틴에 대해서만 완료가 가능합니다.", ErrorCode.BAD_REQUEST);
        }

        if (routineDetail.getCycle().getRpeCycTpCd() == CycleType.DAY) {
            if(!isTargetForDay(routineDetail, requestDate)){
                throw new KBException("등록하신 루틴 주기에 해당하는 날짜가 아닙니다.", ErrorCode.BAD_REQUEST);
            }
        }
    }

    public boolean isTargetForDay(MmrRoutnDtlMgt routineDetail, LocalDate requestDate) {
        return
                switch (requestDate.getDayOfWeek()) {
                    case MONDAY -> routineDetail.getCycle().getMonYn().equals("Y") ? true : false;
                    case TUESDAY -> routineDetail.getCycle().getTueYn().equals("Y") ? true : false;
                    case WEDNESDAY -> routineDetail.getCycle().getWedYn().equals("Y") ? true : false;
                    case THURSDAY -> routineDetail.getCycle().getThuYn().equals("Y") ? true : false;
                    case FRIDAY -> routineDetail.getCycle().getFriYn().equals("Y") ? true : false;
                    case SATURDAY -> routineDetail.getCycle().getSatYn().equals("Y") ? true : false;
                    case SUNDAY -> routineDetail.getCycle().getSunYn().equals("Y") ? true : false;
                };
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

    private void updateRoutineAchievement(MmrRoutnDtlMgt routineDetail,
                                          CreateRoutineAchieveDetailReqDto reqDto,
                                          MmrRoutnAhvHis achievement) {
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

    private MmrRoutnAhvHis createRoutineAchievement(MmrRoutnDtlMgt routineDetail,
                                                    CreateRoutineAchieveDetailReqDto achievementDto,
                                                    LocalDate requestDate) {
        AchieveStatus status = AchieveStatus.N;

        if (routineDetail.getGoal().getGolTpCd().equals(1)) {
            status = AchieveStatus.Y;
        } else if (routineDetail.getGoal().getGolVal() <= achievementDto.getGoal().getGoalValue()) {
            status = AchieveStatus.Y;
        }

        return MmrRoutnAhvHis.createRoutineAchievement(
                requestDate,
                routineDetail.getRoutnChDt(),
                routineDetail.getRoutnSeq(),
                routineDetail.getMmrId(),
                achievementDto.getGoal().toEntity(),
                status
        );
    }


    @Transactional
    public void removeRoutineAchievement(DeleteRoutineAchieveReqDto reqDto){
        mmrRoutnAchHisRepo.bulkDeleteRoutnAhv(reqDto.getRoutineList(), reqDto.getMemberId(), reqDto.getRequestDate());
    }

}
