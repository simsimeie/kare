package com.example.kare.domain.today.dto;

import com.example.kare.entity.routine.Goal;
import com.example.kare.entity.routine.constant.GoalUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class GoalDto {

    @Max(9999999)
    private Integer goalValue;
    private GoalUnit goalUnit;

    public Goal toEntity(){
        return Goal.createGoal(this);
    }


}
