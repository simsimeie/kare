package com.example.kare.domain.routine.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
class RoutineAchievementServiceTest {
    @Autowired
    private RoutineAchievementService routineAchievementService;
    @Test
    public void test01(){
        routineAchievementService.findMonthlyAchievement("8cc4ef71-d8df-4cf7-bdee-3d4cea1d6a17",1, LocalDate.of(2023, Month.JUNE,1));
    }
}