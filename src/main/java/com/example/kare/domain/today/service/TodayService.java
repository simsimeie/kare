package com.example.kare.domain.today.service;

import com.example.kare.domain.calendar.dto.SearchCriteriaDto;
import com.example.kare.domain.calendar.dto.StatisticsDto;
import com.example.kare.domain.calendar.service.CalendarService;
import com.example.kare.domain.today.dto.RoutineGroupResDto;
import com.example.kare.domain.today.dto.RoutineResDto;
import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.entity.routine.constant.AchieveStatus;
import com.example.kare.repository.MmrRoutnAchHisRepo;
import com.example.kare.repository.MmrRoutnDtlMgtRepo;
import com.example.kare.repository.MmrRoutnMgtRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TodayService {
    private final MmrRoutnMgtRepo mmrRoutnMgtRepo;
    private final MmrRoutnDtlMgtRepo mmrRoutnDtlMgtRepo;
    private final MmrRoutnAchHisRepo mmrRoutnAchHisRepo;
    private final CalendarService calendarService;

    public Map<String, Object> findTodayRoutines(String memberId, LocalDate searchDate) {
        Map<String, Object> result = new HashMap<>();

        List<RoutineResDto> routineList = new ArrayList<>();
        Set<RoutineGroupResDto> routineGroupSet = new HashSet<>();

        List<RoutineResDto> todayRoutineList = mmrRoutnMgtRepo.findTodayRoutnList(memberId, searchDate);

        if (todayRoutineList.isEmpty()) {
            result.put("routineList", routineList);
            result.put("routineGroupList", routineGroupSet);
            return result;
        }

        Set<Integer> routineSeqSet = todayRoutineList.stream().map(RoutineResDto::getRoutineSequence).collect(Collectors.toSet());

        // 이번주 시작일, 종료일 추출
        SearchCriteriaDto weekCriteria = calendarService.getWeekCriteria(searchDate);

        // 목표일 추출 로직
        // 시작일 ~ 종료일에 해당하는 루틴 상세 추출
        List<MmrRoutnDtlMgt> routineDetailList = mmrRoutnDtlMgtRepo.findValidRoutnDtlList(
                memberId,
                weekCriteria.getStartDate(),
                weekCriteria.getEndDate(),
                todayRoutineList.stream().map(RoutineResDto::getRoutineSequence).collect(Collectors.toSet())
        );

        Map<Integer, StatisticsDto> targetStatMap = calendarService.calculateTargetStat(memberId, routineDetailList, weekCriteria);

        // 달성일 추출 로직
        List<MmrRoutnAhvHis> routineAchievementList = mmrRoutnAchHisRepo.findRoutnAchList(
                memberId,
                weekCriteria.getStartDate(),
                weekCriteria.getEndDate(),
                routineSeqSet
        );

        List<MmrRoutnAhvHis> completed = routineAchievementList.stream()
                .filter(achievement -> achievement.getGolAhvYn() == AchieveStatus.Y)
                .collect(Collectors.toList());

        Map<Integer, StatisticsDto> completeStatMap = calendarService.calculateCompleteStat(memberId, completed);

        // Grouping
        // key : 루틴그룹Sequence value : 루틴그룹에 포함된 루틴 List
        Map<Integer, List<RoutineResDto>> routineMapByRoutineGroup = new HashMap<>();

        todayRoutineList.forEach(routine -> {
            if (targetStatMap.get(routine.getRoutineSequence()) != null) {
                routine.setTargetDaysNumPerWeek(targetStatMap.get(routine.getRoutineSequence()).getNum());
            }
            if (completeStatMap.get(routine.getRoutineSequence()) != null) {
                routine.setCompleteDaysNumPerWeek(completeStatMap.get(routine.getRoutineGroupSequence()).getNum());
            }

            if (null != routine.getRoutineGroupSequence()) {
                routineGroupSet.add(new RoutineGroupResDto(routine));
                List<RoutineResDto> list = Optional.ofNullable(routineMapByRoutineGroup.get(routine.getRoutineGroupSequence())).orElseGet(ArrayList::new);
                list.add(routine);
                routineMapByRoutineGroup.put(routine.getRoutineGroupSequence(), list);
            } else {
                routineList.add(routine);
            }
        });

        // 그룹 정보 생성
        for (RoutineGroupResDto group : routineGroupSet) {
            List<RoutineResDto> routineListInGroup = routineMapByRoutineGroup.get(group.getRoutineGroupSequence());
            group.setRoutineList(routineListInGroup);

            group.setTotalRoutineNum(routineListInGroup.size());

            group.setCompleteRoutineNum(
                    (int) routineListInGroup.stream()
                            .filter(routineResDto -> routineResDto.isAchievementStatus() == true).count()
            );

            boolean completeStatus = group.getTotalRoutineNum() <= group.getCompleteRoutineNum() ? true : false;
            group.setRoutineGroupCompleteStatus(completeStatus);
        }

        result.put("routineList", routineList);
        result.put("routineGroupList", routineGroupSet);

        return result;
    }

    private void calculateCompleteDaysNum(String memberId, Map<Integer, RoutineResDto> todayRoutineMap, SearchCriteriaDto weekCriteria) {
        List<MmrRoutnAhvHis> routineAchievementList = mmrRoutnAchHisRepo.findRoutnAchList(
                memberId,
                weekCriteria.getStartDate(),
                weekCriteria.getEndDate(),
                todayRoutineMap.keySet()
        );

        List<MmrRoutnAhvHis> completed = routineAchievementList.stream()
                .filter(achievement -> achievement.getGolAhvYn().equals("Y"))
                .collect(Collectors.toList());

        completed.forEach(achievement -> {
            RoutineResDto routineResDto = todayRoutineMap.get(achievement.getRoutnSeq());
            routineResDto.setCompleteDaysNumPerWeek(routineResDto.getCompleteDaysNumPerWeek() + 1);
        });
    }
}
