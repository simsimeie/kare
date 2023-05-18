package com.example.kare.domain.calendar.service;

import com.example.kare.domain.calendar.dto.DateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class CalendarServiceTest {

    @Test
    @DisplayName("주초 주말을 잘가져오는지 테스트")
    public void getWeekCriteriaTest01(){
        CalendarService calculator = new CalendarService();
        //given && when
        DateDto weekCriteria = calculator.getWeekCriteria(LocalDate.of(2023, Month.JUNE, 1));
        //then
        assertEquals(LocalDate.of(2023,Month.MAY,29), weekCriteria.getStartDate());
        assertEquals(LocalDate.of(2023,Month.JUNE,4), weekCriteria.getEndDate());
    }

}