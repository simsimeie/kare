package com.example.kare.entity.routine;

import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.entity.routine.constant.CycleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class CycleTest {

    @Test
    @DisplayName("정상 주기 형태가 들어왔을 때 정상적으로 Cycle 엔티티를 반환하는지 테스트")
    public void createCycleTest01(){
        //given
        CycleDto cycleDto = new CycleDto(CycleType.DAY, true, true, true, true, false, false, true);
        //when
        Cycle cycle = cycleDto.toEntity();
        //then
        assertEquals(CycleType.DAY, cycle.getCycleType());
        assertEquals(true, cycle.isMon());
        assertEquals(true, cycle.isTue());
        assertEquals(true, cycle.isWed());
        assertEquals(true, cycle.isThu());
        assertEquals(false, cycle.isFri());
        assertEquals(false, cycle.isSat());
        assertEquals(true, cycle.isSun());
    }


    @Test
    @DisplayName("정상 주기 형태가 들어왔을 때 정상적으로 Cycle 엔티티를 반환하는지 테스트")
    public void createCycleTest02(){
        //given
        CycleDto cycleDto = new CycleDto(CycleType.TIMES, 3);
        //when
        Cycle cycle = cycleDto.toEntity();
        //then
        assertEquals(CycleType.TIMES, cycle.getCycleType());
        assertEquals(3, cycle.getCount());
    }

    @Test
    @DisplayName("주당 일수 반복주기 임에도 횟수 데이터가 들어오지 않았을 때 Exception 발생하는지 테스트")
    public void createCycleTest03(){
        //given
        CycleDto cycleDto = new CycleDto(CycleType.TIMES, null);
        //when
        Exception exception = assertThrows(KBException.class , () -> cycleDto.toEntity());
    }

    @Test
    @DisplayName("특정 요일 반복주기 임에도 요일 데이터가 1건도 들어오지 않았을 때 Exception 발생하는지 테스트")
    public void createCycleTest04(){
        //given
        CycleDto cycleDto = new CycleDto(CycleType.DAY, false,false,false,false,false,false,false);
        //when
        Exception exception = assertThrows(KBException.class , () -> cycleDto.toEntity());
    }

}