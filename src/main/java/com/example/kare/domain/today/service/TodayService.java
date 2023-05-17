package com.example.kare.domain.today.service;

import com.example.kare.domain.calendar.dto.DateDto;
import com.example.kare.domain.calendar.service.Calculator;
import com.example.kare.domain.today.dto.RoutineGroupResDto;
import com.example.kare.domain.today.dto.RoutineResDto;
import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.repository.MmrRoutnAchHisRepo;
import com.example.kare.repository.MmrRoutnDtlMgtRepo;
import com.example.kare.repository.MmrRoutnMgtRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TodayService {
    private final MmrRoutnMgtRepo mmrRoutnMgtRepo;
    private final MmrRoutnDtlMgtRepo mmrRoutnDtlMgtRepo;
    private final MmrRoutnAchHisRepo mmrRoutnAchHisRepo;
    private final Calculator calculator;

    public Map<String, Object> findTodayRoutines(String memberId, LocalDate searchDate) {
        List<RoutineResDto> routineList = new ArrayList<>();
        Set<RoutineGroupResDto> routineGroupSet = new HashSet<>();
        Map<Integer, List<RoutineResDto>> mapByRoutineGroup = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        List<RoutineResDto> todayRoutineList = mmrRoutnMgtRepo.findTodayRoutnList(memberId, searchDate);

        // 루틴Sequence 기준 Map 생성
        Map<Integer, RoutineResDto> routineDtoMap = new HashMap<>();
        todayRoutineList.forEach(dto -> routineDtoMap.put(dto.getRoutineSequence(), dto));


        // 목표일 추출 로직
        // 이번주 시작일, 종료일 추출
        DateDto weekCriteria = calculator.getWeekCriteria(searchDate);
        // 시작일 ~ 종료일에 해당하는 루틴 상세 추출
        List<MmrRoutnDtlMgt> routineDetailList = mmrRoutnDtlMgtRepo.findValidRoutnDtlList(
                memberId,
                weekCriteria.getStartDate(),
                weekCriteria.getEndDate(),
                routineDtoMap.keySet()
        );

        // 상세 루프 돌면서
        // max(루틴변경일자, 이번주 시작일, 루틴시작일) <= 루틴상세변경일자 <= min(다음 루틴상세의 변경일자, 이번주 종료일, 루틴 종료일)
        Integer nextRoutineSeq = 0;
        LocalDate nextRoutineDetailChangeDate = LocalDate.of(9999, Month.DECEMBER, 31);
        LocalDate startCriteria;
        LocalDate endCriteria = weekCriteria.getEndDate();

        ListIterator<MmrRoutnDtlMgt> iterator = routineDetailList.listIterator(routineDetailList.size());
        while (iterator.hasPrevious()) {
            MmrRoutnDtlMgt routineDetail = iterator.previous();

            // max(루틴변경일자, 이번주 시작일)
            startCriteria = weekCriteria.getStartDate().isAfter(routineDetail.getRoutnChDt()) ? weekCriteria.getStartDate() : routineDetail.getRoutnChDt();

            if (routineDetail.getRoutnSeq().equals(nextRoutineSeq)) {
                //min(다음 루틴상세의 변경일자, 이번주 종료일)
                endCriteria = endCriteria.isBefore(nextRoutineDetailChangeDate) ? endCriteria : nextRoutineDetailChangeDate.minusDays(1);
            }

            int targetDatesNum = routineDetail.getTargetDaysNum(startCriteria, endCriteria);
            RoutineResDto routineResDto = routineDtoMap.get(routineDetail.getRoutnSeq());
            routineResDto.setTargetDaysNumPerWeek(routineResDto.getTargetDaysNumPerWeek() + targetDatesNum);

            nextRoutineSeq = routineDetail.getRoutnSeq();
            nextRoutineDetailChangeDate = routineDetail.getRoutnChDt();
        }

        // 달성일 추출 로직
        List<MmrRoutnAhvHis> completedAchievementList = mmrRoutnAchHisRepo.findCompletedRoutnAchList(
                memberId,
                weekCriteria.getStartDate(),
                weekCriteria.getEndDate(),
                routineDtoMap.keySet()
        );

        completedAchievementList.forEach(achievement -> {
            RoutineResDto routineResDto = routineDtoMap.get(achievement.getRoutnSeq());
            routineResDto.setCompleteDaysNumPerWeek(routineResDto.getCompleteDaysNumPerWeek() + 1);
        });


        // Grouping
        todayRoutineList.forEach(routine -> {
            if (null != routine.getRoutineGroupSequence()) {
                routineGroupSet.add(new RoutineGroupResDto(routine.getRoutineGroupSequence(), routine.getRoutineGroupName()));
                List<RoutineResDto> list = Optional.ofNullable(mapByRoutineGroup.get(routine.getRoutineGroupSequence())).orElseGet(ArrayList::new);
                list.add(routine);
                mapByRoutineGroup.put(routine.getRoutineGroupSequence(), list);
            } else {
                routineList.add(routine);
            }
        });

        for (RoutineGroupResDto group : routineGroupSet) {
            List<RoutineResDto> routinesInGroup = mapByRoutineGroup.get(group.getRoutineGroupSequence());
            group.setRoutines(routinesInGroup);
            group.setTotalRoutineNum(routinesInGroup.size());
            group.setCompleteRoutineNum(
                    (int) routinesInGroup
                            .stream()
                            .filter(routineResDto -> routineResDto.isAchievementStatus() == true)
                            .count()
            );

            boolean completeStatus = group.getTotalRoutineNum() <= group.getCompleteRoutineNum() ? true : false;
            group.setRoutineGroupCompleteStatus(completeStatus);
            group.setSortOrder(routinesInGroup.get(0).getRoutineGroupSortOrder());
        }

        result.put("routineList", routineList);
        result.put("routineGroupList", routineGroupSet);

        return result;
    }
}
