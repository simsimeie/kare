package com.example.kare.domain.routine.service;

import com.example.kare.domain.achievement.service.RoutineAchievementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;

@SpringBootTest
class RoutineAchievementServiceTest {
    @Autowired
    private RoutineAchievementService routineAchievementService;
    @Test
    public void test01(){
        routineAchievementService.findMonthlyAchievementStat("373e4f47-78ca-48ab-b7e9-e0d25885adbc",3, LocalDate.of(2023, Month.MAY,1));
    }
}