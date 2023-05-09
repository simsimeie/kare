package com.example.kare.domain.today.dto;

import com.example.kare.entity.routine.value.Goal;
import com.example.kare.entity.routine.constant.GoalUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class GoalDto {
    private Integer goalType;
    @Max(value = 9999999, message = "최대 7자리 정수까지만 가능합니다.")
    @Positive(message = "목표 값은 0보다 큰 양수로 입력하셔야 합니다.")
    private Integer goalValue;
    private GoalUnit goalUnit;

    public Goal toEntity(){
        return Goal.createGoal(this);
    }


}
