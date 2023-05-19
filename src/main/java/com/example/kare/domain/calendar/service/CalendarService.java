package com.example.kare.domain.calendar.service;

import com.example.kare.domain.calendar.dto.SearchCriteriaDto;
import com.example.kare.domain.calendar.dto.StatisticsDto;
import com.example.kare.domain.today.dto.RoutineResDto;
import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class CalendarService {
    private final DayOfWeek firstDay;
    private final DayOfWeek endDay;

    public CalendarService(){
        this.firstDay = DayOfWeek.MONDAY;
        this.endDay = DayOfWeek.SUNDAY;
    }
    public SearchCriteriaDto getWeekCriteria(LocalDate searchDate){
        LocalDate firstDate = searchDate.with(TemporalAdjusters.previousOrSame(firstDay));
        LocalDate endDate = searchDate.with(TemporalAdjusters.nextOrSame(endDay));

        return SearchCriteriaDto.of(firstDate, endDate);
    }

    public SearchCriteriaDto getMonthCriteria(LocalDate searchDate){
        LocalDate firstDate = searchDate.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endDate = searchDate.with(TemporalAdjusters.lastDayOfMonth());

        return SearchCriteriaDto.of(firstDate, endDate);
    }

    public Map<Integer, StatisticsDto> calculateTargetStat(String memberId, List<MmrRoutnDtlMgt> routineDetailList, SearchCriteriaDto criteria) {
        int targetDaysNum;
        List<LocalDate> targetDateList;
        Map<Integer, StatisticsDto> resultMap = new HashMap<>();

        // 상세 루프 돌면서
        // max(루틴변경일자, 이번주 시작일, 루틴시작일) <= 루틴상세변경일자 <= min(다음 루틴상세의 변경일자, 이번주 종료일, 루틴 종료일)
        Integer nextRoutineSeq = 0;
        LocalDate nextRoutineDetailChangeDate = LocalDate.of(9999, Month.DECEMBER, 31);
        LocalDate startCriteria;
        LocalDate endCriteria = criteria.getEndDate();

        ListIterator<MmrRoutnDtlMgt> iterator = routineDetailList.listIterator(routineDetailList.size());
        while (iterator.hasPrevious()) {
            MmrRoutnDtlMgt routineDetail = iterator.previous();

            // max(루틴변경일자, 이번주 시작일)
            startCriteria = criteria.getStartDate().isAfter(routineDetail.getRoutnChDt()) ? criteria.getStartDate() : routineDetail.getRoutnChDt();

            if (routineDetail.getRoutnSeq().equals(nextRoutineSeq)) {
                //min(다음 루틴상세의 변경일자, 이번주 종료일)
                endCriteria = endCriteria.isBefore(nextRoutineDetailChangeDate) ? endCriteria : nextRoutineDetailChangeDate.minusDays(1);
            }

            targetDaysNum = routineDetail.getTargetDaysNum(startCriteria, endCriteria);
            targetDateList = routineDetail.getTargetDates(startCriteria, endCriteria);

            StatisticsDto statisticsDto = Optional.ofNullable(resultMap.get(routineDetail.getRoutnSeq())).orElseGet(StatisticsDto::new);
            statisticsDto.setNum(statisticsDto.getNum() + targetDaysNum);
            statisticsDto.getDateList().addAll(targetDateList);
            resultMap.put(routineDetail.getRoutnSeq(), statisticsDto);

            nextRoutineSeq = routineDetail.getRoutnSeq();
            nextRoutineDetailChangeDate = routineDetail.getRoutnChDt();
        }

        return resultMap;
    }


    public Map<Integer, StatisticsDto> calculateCompleteStat(String memberId, List<MmrRoutnAhvHis> completedList) {
        Map<Integer, StatisticsDto> resultMap = new HashMap<>();

        completedList.forEach(achievement -> {
            StatisticsDto statisticsDto = Optional.ofNullable(resultMap.get(achievement.getRoutnSeq())).orElseGet(StatisticsDto::new);
            statisticsDto.setNum(statisticsDto.getNum() + 1);
            statisticsDto.getDateList().add(achievement.getRoutnAhvDt());
            resultMap.put(achievement.getRoutnSeq(), statisticsDto);
        });

        return resultMap;
    }
}
