package com.example.kare.domain.today.dto;


import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RoutineResponseDto {
    private Long routineId;
    private String routineName;
    private String dayOfWeek;
    private Integer cycleCount;
    private CycleType cycleType;
    private Integer goalValue;
    private GoalUnit goalUnit;
    private Long routineGroupId;
    private String routineGroupName;
    private Integer displayOrder;
}
