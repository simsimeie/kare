package com.example.kare.entity.routine;

import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class RoutineHistoryTest {

    @Test
    @DisplayName("루틴의 사이클, 목표, startDate가 동일할 때는 isShouldUpdateRoutineHistory 메서드의 결과가 false가 나오는지 테스트")
    public void isShouldUpdateRoutineHistoryTest01(){
        //given
        Cycle cycle = CycleDto.of(CycleType.TIMES, false, false, false, false, false, false, false, 10).toEntity();
        Goal goal = GoalDto.of(10, GoalUnit.TIMES).toEntity();
        LocalDate startDate = LocalDate.now();

        Routine mockRoutine = Mockito.mock(Routine.class);
        given(mockRoutine.getCycle()).willReturn(cycle);
        given(mockRoutine.getGoal()).willReturn(goal);
        given(mockRoutine.getStartDate()).willReturn(startDate);

        RoutineHistory routineHistory = RoutineHistory.createRoutineHistory(mockRoutine, startDate);

        //when
        boolean result = routineHistory.isShouldUpdateRoutineHistory(mockRoutine);

        //then
        assertEquals(false, result);
    }

    @Test
    @DisplayName("루틴의 사이클 값이 다를 때 isShouldUpdateRoutineHistory 메서드의 결과가 true 나오는지 테스트")
    public void isShouldUpdateRoutineHistoryTest02(){
        //given
        Cycle cycle1 = CycleDto.of(CycleType.TIMES, false, false, false, false, false, false, false, 10).toEntity();
        Cycle cycle2 = CycleDto.of(CycleType.DAY, false, false, false, true, false, false, false, null).toEntity();

        Goal goal = GoalDto.of(10, GoalUnit.TIMES).toEntity();
        LocalDate startDate = LocalDate.now();

        Routine mockRoutine = Mockito.mock(Routine.class);
        given(mockRoutine.getCycle()).willReturn(cycle1);
        given(mockRoutine.getGoal()).willReturn(goal);
        given(mockRoutine.getStartDate()).willReturn(startDate);

        RoutineHistory routineHistory = RoutineHistory.createRoutineHistory(mockRoutine, startDate);
        given(mockRoutine.getCycle()).willReturn(cycle2);

        //when
        boolean result = routineHistory.isShouldUpdateRoutineHistory(mockRoutine);

        //then
        assertEquals(true, result);
    }

    @Test
    @DisplayName("루틴의 목표가 다를 때는 isShouldUpdateRoutineHistory 메서드의 결과가 true가 나오는지 테스트")
    public void isShouldUpdateRoutineHistoryTest03(){
        //given
        Cycle cycle = CycleDto.of(CycleType.TIMES, false, false, false, false, false, false, false, 10).toEntity();

        Goal goal1 = GoalDto.of(10, GoalUnit.TIMES).toEntity();
        Goal goal2 = GoalDto.of(10, GoalUnit.L).toEntity();

        LocalDate startDate = LocalDate.now();

        Routine mockRoutine = Mockito.mock(Routine.class);
        given(mockRoutine.getCycle()).willReturn(cycle);
        given(mockRoutine.getGoal()).willReturn(goal1);
        given(mockRoutine.getStartDate()).willReturn(startDate);

        RoutineHistory routineHistory = RoutineHistory.createRoutineHistory(mockRoutine, startDate);
        given(mockRoutine.getGoal()).willReturn(goal2);

        //when
        boolean result = routineHistory.isShouldUpdateRoutineHistory(mockRoutine);

        //then
        assertEquals(true, result);
    }

    @Test
    @DisplayName("루틴의 시작시간이 다를 때는 isShouldUpdateRoutineHistory 메서드의 결과가 true가 나오는지 테스트")
    public void isShouldUpdateRoutineHistoryTest04(){
        //givien
        Cycle cycle = CycleDto.of(CycleType.TIMES, false, false, false, false, false, false, false, 10).toEntity();
        Goal goal = GoalDto.of(10, GoalUnit.TIMES).toEntity();
        LocalDate startDate = LocalDate.now();

        Routine mockRoutine = Mockito.mock(Routine.class);
        given(mockRoutine.getCycle()).willReturn(cycle);
        given(mockRoutine.getGoal()).willReturn(goal);
        given(mockRoutine.getStartDate()).willReturn(startDate);

        RoutineHistory routineHistory = RoutineHistory.createRoutineHistory(mockRoutine, startDate);
        given(mockRoutine.getStartDate()).willReturn(LocalDate.now().minusDays(1));

        //when
        boolean result = routineHistory.isShouldUpdateRoutineHistory(mockRoutine);

        //then
        assertEquals(true, result);
    }

}