package com.example.kare.domain.today.dto;


import com.example.kare.domain.routine.dto.CycleDto;
import com.example.kare.domain.routine.dto.GoalDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Integer routineGroupSortOrder;
    private int targetDatesNum;
    @JsonIgnore
    private Integer routineGroupSequence;
    @JsonIgnore
    private String routineGroupName;

}
