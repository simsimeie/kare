package com.example.kare.domain.routine.dto;

import com.example.kare.entity.routine.constant.GoalUnitTypeCode;

import static org.junit.jupiter.api.Assertions.*;

public class GoalDtoTest {
    public static GoalDto createGoalTypeOne(){
        return GoalDto.of(1, null, null);
    }

    public static GoalDto createGoalTypeTwo(Integer value){
        return GoalDto.of(2, GoalUnitTypeCode.MIN, value);
    }

    public static GoalDto createGoalTypeThree(GoalUnitTypeCode unitTypeCode, Integer value){
        return GoalDto.of(3, unitTypeCode, value);
    }

}