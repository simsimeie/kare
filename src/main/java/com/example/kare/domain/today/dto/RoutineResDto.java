package com.example.kare.domain.today.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RoutineResDto {
    private Integer routineSequence;
    private String routineName;
    private CycleDto cycle;
    private GoalDto goal;
    private Integer achievementValue;
    private boolean achievementStatus;
    private Integer sortOrder;
    private Integer routineGroupSequence;
    private String routineGroupName;
    private Integer routineGroupSortOrder;
}
