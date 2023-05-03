package com.example.kare.entity.routine;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.entity.routine.constant.GoalUnit;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;

@Embeddable
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Goal {
    private Integer goalValue;
    @Enumerated(EnumType.STRING)
    private GoalUnit goalUnit;
    private Integer goalType;

    private Goal(Integer goalValue, GoalUnit goalUnit) {
        this.goalValue = goalValue;
        if(null == goalUnit) {
            this.goalUnit = GoalUnit.TIMES;
        }
        else {
            this.goalUnit = goalUnit;
        }
    }

    // ******** 생성 함수 ********
    public static Goal createGoal(GoalDto goalDto){
        checkValidGoalUnit(goalDto);

        Goal goal = new Goal();
        goal.setGoalType(goalDto.getGoalType());
        goal.setGoalValue(goalDto.getGoalValue());
        goal.setGoalUnit(goalDto.getGoalUnit());

        /*
        if(null == goalUnit) {
            this.goalUnit = GoalUnit.TIMES;
        }
        else {
            this.goalUnit = goalUnit;
        }
         */
        return goal;
    }

    private static void checkValidGoalUnit(GoalDto goalDto) {
        for(GoalUnit unit : GoalUnit.values()){
            if(unit == goalDto.getGoalUnit()) return;
        }
        throw new KBException("존재하지 않는 단위 입니다.", ErrorCode.BAD_REQUEST);
    }
}
