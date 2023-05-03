package com.example.kare.domain.today.dto;


import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RoutineResponseDto {
    private Integer routineSequence;
    private String routineName;
    private String dayOfWeek;
    private CycleDto cycle;
    private GoalDto goal;
    private Integer routineGroupSequence;
    private String routineGroupName;
    private Integer sortOrder;


}
