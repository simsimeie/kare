package com.example.kare.domain.calendar.service;

import com.example.kare.domain.calendar.dto.DateDto;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

@Component
public class CalendarService {
    private final DayOfWeek firstDay;
    private final DayOfWeek endDay;

    public CalendarService(){
        this.firstDay = DayOfWeek.MONDAY;
        this.endDay = DayOfWeek.SUNDAY;
    }
    public DateDto getWeekCriteria(LocalDate searchDate){
        LocalDate firstDate = searchDate.with(TemporalAdjusters.previousOrSame(firstDay));
        LocalDate endDate = searchDate.with(TemporalAdjusters.nextOrSame(endDay));

        return DateDto.of(firstDate, endDate);
    }

    public DateDto getMonthCriteria(LocalDate searchDate){
        LocalDate firstDate = searchDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = searchDate.with(TemporalAdjusters.lastDayOfMonth());

        return DateDto.of(firstDate, endDate);
    }
}
