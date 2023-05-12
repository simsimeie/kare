package com.example.kare.domain.calendar.service;

import com.example.kare.domain.calendar.dto.DateDto;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;

@Component
public class Calculator {
    private final DayOfWeek firstDay;
    private final DayOfWeek endDay;

    public Calculator(){
        this.firstDay = DayOfWeek.MONDAY;
        this.endDay = DayOfWeek.SUNDAY;
    }
    public DateDto getWeekCriteria(LocalDate searchDate){
        LocalDate firstDate = searchDate.with(TemporalAdjusters.previousOrSame(firstDay));
        LocalDate endDate = searchDate.with(TemporalAdjusters.nextOrSame(endDay));

        return DateDto.of(firstDate, endDate);
    }
}
