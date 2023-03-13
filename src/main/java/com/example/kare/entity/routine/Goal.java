package com.example.kare.entity.routine;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.entity.routine.constant.GoalUnit;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Goal {
    private Integer goalValue;
    @Enumerated(EnumType.STRING)
    private GoalUnit goalUnit;

    public Goal(Integer goalValue, GoalUnit goalUnit) {
        this.goalValue = goalValue;
        if(null == goalUnit) {
            this.goalUnit = GoalUnit.TIMES;
        }
        else {
            this.goalUnit = goalUnit;
        }
    }
    public static Goal createGoal(GoalDto goalDto){
        checkValidGoalUnit(goalDto);
        return new Goal(goalDto.getGoalValue(), goalDto.getGoalUnit());
    }

    private static void checkValidGoalUnit(GoalDto goalDto) {
        for(GoalUnit unit : GoalUnit.values()){
            if(unit == goalDto.getGoalUnit()) return;
        }
        throw new KBException("존재하지 않는 단위 입니다.", ErrorCode.BAD_REQUEST);
    }
}
