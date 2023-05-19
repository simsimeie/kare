package com.example.kare.domain.achievement.dto;


import com.example.kare.domain.routine.dto.CycleDto;
import com.example.kare.domain.routine.dto.GoalDto;
import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.entity.routine.value.Cycle;
import com.example.kare.entity.routine.value.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RetrieveAchievementResDto {
    private Integer routineSequence;
    private String routineName;
    private Integer elapsedDays;
    private CycleDto repeatCycle;
    private GoalDto goal;

    public RetrieveAchievementResDto(MmrRoutnDtlMgt mmrRoutnDtlMgt){
        this.routineSequence = mmrRoutnDtlMgt.getRoutnSeq();
        this.routineName = mmrRoutnDtlMgt.getRoutnNm();

       this.elapsedDays =
               Period.between(mmrRoutnDtlMgt.getStDt(), LocalDate.now()).getDays() + 1;

        Cycle cycleEntity = mmrRoutnDtlMgt.getCycle();
        this.repeatCycle = CycleDto.of(
                cycleEntity.getRpeCycTpCd(),
                cycleEntity.getMonYn(),
                cycleEntity.getTueYn(),
                cycleEntity.getWedYn(),
                cycleEntity.getThuYn(),
                cycleEntity.getFriYn(),
                cycleEntity.getSatYn(),
                cycleEntity.getSunYn(),
                cycleEntity.getWkDcn()
        );

        Goal goalEntity = mmrRoutnDtlMgt.getGoal();
        this.goal = GoalDto.of(
                goalEntity.getGolTpCd(),
                goalEntity.getGolUnitTpCd(),
                goalEntity.getGolVal()
        );
    }
}
