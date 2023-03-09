package com.example.kare.entity.routine;

import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.entity.routine.constant.GoalUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GoalTest {

    @Test
    @DisplayName("정상 목표 단위가 들어왔을 때 정상적으로 Goal 엔티티를 반환하는지 테스트")
    public void createGoalTest01(){
        //given
        GoalDto goalDto = GoalDto.of(100, GoalUnit.L);
        //when
        Goal goal = goalDto.toEntity();
        //then
        assertEquals(GoalUnit.L, goal.getGoalUnit());
        assertEquals(100, goal.getGoalValue());
    }

    @Test
    @DisplayName("정상 목표 단위가 들어왔을 때 정상적으로 Goal 엔티티를 반환하는지 테스트")
    public void createGoalTest02(){
        //given
        GoalDto goalDto = GoalDto.of(100, GoalUnit.WON);
        //when
        Goal goal = goalDto.toEntity();
        //then
        assertEquals(GoalUnit.WON, goal.getGoalUnit());
        assertEquals(100, goal.getGoalValue());
    }

    @Test
    @DisplayName("정상 목표 단위가 들어왔을 때 정상적으로 Goal 엔티티를 반환하는지 테스트")
    public void createGoalTest03(){
        //given
        GoalDto goalDto = GoalDto.of(100, GoalUnit.TIMES);
        //when
        Goal goal = goalDto.toEntity();
        //then
        assertEquals(GoalUnit.TIMES, goal.getGoalUnit());
        assertEquals(100, goal.getGoalValue());
    }
}