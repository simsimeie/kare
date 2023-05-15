package com.example.kare.domain.routine.dto;

import com.example.kare.entity.routine.constant.CycleType;

import static org.junit.jupiter.api.Assertions.*;

public class CycleDtoTest {
    public static CycleDto createCycleDtoTimesType(Integer value){
        return new CycleDto(CycleType.TIMES, value);
    }
    public static CycleDto createCycleDtoDayTypeBusinessDay(){
        return new CycleDto(CycleType.DAY,
                "Y",
                "Y",
                "Y",
                "Y",
                "Y",
                "N",
                "N");
    }
    public static CycleDto createCycleDtoDayTypeWeekend(){
        return new CycleDto(CycleType.DAY,
                "N",
                "N",
                "N",
                "N",
                "N",
                "Y",
                "Y");
    }
}