package com.example.kare.entity.routine.value;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.routine.dto.GoalDto;
import com.example.kare.entity.routine.constant.GoalUnitTypeCode;
import lombok.*;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Goal {
    private Integer golTpCd;
    @Enumerated(EnumType.STRING)
    private GoalUnitTypeCode golUnitTpCd;
    private Integer golVal;

    private Goal(Integer goalValue, GoalUnitTypeCode goalUnitTypeCode) {
        this.golVal = goalValue;
        if(null == goalUnitTypeCode) {
            this.golUnitTpCd = GoalUnitTypeCode.TIMES;
        }
        else {
            this.golUnitTpCd = goalUnitTypeCode;
        }
    }

    // ******** 생성 함수 ********
    public static Goal createGoal(GoalDto goalDto){
        if(!goalDto.getGoalTypeCode().equals(1)) {
            checkValidGoalUnit(goalDto.getGoalUnitTypeCode());
        }

        Goal goal = new Goal();
        goal.setGolTpCd(goalDto.getGoalTypeCode());
        goal.setGolVal(goalDto.getGoalValue());
        goal.setGolUnitTpCd(goalDto.getGoalUnitTypeCode());

        return goal;
    }

    // ******** 비즈니스 로직 ********
    private static void checkValidGoalUnit(GoalUnitTypeCode goalUnitTypeCode) {
        for(GoalUnitTypeCode unit : GoalUnitTypeCode.values()){
            if(unit == goalUnitTypeCode) return;
        }
        throw new KBException("존재하지 않는 단위 입니다.", ErrorCode.BAD_REQUEST);
    }
}
