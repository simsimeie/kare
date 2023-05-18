package com.example.kare.domain.recomend.dto;

import com.example.kare.domain.routine.dto.CycleDto;
import com.example.kare.domain.routine.dto.GoalDto;
import com.example.kare.entity.routine.ReciRoutnMgt;
import com.example.kare.entity.routine.value.Cycle;
import com.example.kare.entity.routine.value.Goal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecommendRoutineResDto {
    private Integer recommendRoutineSeq;
    private String routineName;
    private CycleDto repeatCycle;
    private GoalDto goal;
    private Integer sortOrder;

    public RecommendRoutineResDto(ReciRoutnMgt reciRoutnMgt, Integer sortOrder) {
        this.recommendRoutineSeq = reciRoutnMgt.getReciRoutnSeq();
        this.routineName = reciRoutnMgt.getRoutnNm();

        Cycle cycleEntity = reciRoutnMgt.getRepeatCycle();
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

        Goal goalEntity = reciRoutnMgt.getGoal();
        this.goal = GoalDto.of(
                goalEntity.getGolTpCd(),
                goalEntity.getGolUnitTpCd(),
                goalEntity.getGolVal()
        );

        this.sortOrder = sortOrder;
    }
}
