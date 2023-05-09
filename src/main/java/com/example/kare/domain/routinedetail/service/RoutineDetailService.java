package com.example.kare.domain.routinedetail.service;

import com.example.kare.domain.routinedetail.dto.CreateRoutineAchieveDetailReqDto;
import com.example.kare.domain.routinedetail.dto.CreateRoutineAchieveReqDto;
import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.entity.routine.MmrRoutnMgt;
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
    public void inputRoutineAchievement(CreateRoutineAchieveReqDto reqDto) {
        for (CreateRoutineAchieveDetailReqDto achievementDto : reqDto.getRoutineList()) {
            MmrRoutnDtlMgt routineDetail = mmrRoutnDtlMgtRepo.findValidRoutineDetail(
                    achievementDto.getRoutineSequence(),
                    achievementDto.getMemberId(),
                    achievementDto.getRequestDate()
            );

            if(!isRoutineAchievementPresent(routineDetail, achievementDto.getRequestDate()) &&
                    // requestDate >= startDate 인 경우에만 완료 가능
                    !Period.between(routineDetail.getStartDate(), achievementDto.getRequestDate()).isNegative()){
                MmrRoutnAhvHis routineAchievement = createRoutineAchievement(routineDetail, achievementDto.getRequestDate());
                mmrRoutnAchHisRepo.save(routineAchievement);
            }
        }
    }

    private boolean isRoutineAchievementPresent(MmrRoutnDtlMgt routineDetail, LocalDate requestDate){
        MmrRoutnAhvHisId id = new MmrRoutnAhvHisId(
                requestDate,
                routineDetail.getRoutnChDt(),
                routineDetail.getRoutnSeq(),
                routineDetail.getMmrId()
        );
        return mmrRoutnAchHisRepo.findById(id).isPresent();
    }

    private MmrRoutnAhvHis createRoutineAchievement(MmrRoutnDtlMgt routineDetail, LocalDate requestDate) {
        return MmrRoutnAhvHis.createRoutineAchievement(
                requestDate,
                routineDetail.getRoutnChDt(),
                routineDetail.getRoutnSeq(),
                routineDetail.getMmrId(),
                routineDetail.getGoal()
        );
    }

    @Transactional
    public void deleteRoutine(Integer routnSeq, String memberId) {
        Optional<MmrRoutnMgt> routine = mmrRoutnMgtRepo.findById(new MmrRoutnMgtId(routnSeq, memberId));
        if (routine.isPresent()) {
            mmrRoutnMgtRepo.delete(routine.get());
        }
    }
}
