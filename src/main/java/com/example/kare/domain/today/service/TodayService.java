package com.example.kare.domain.today.service;

import com.example.kare.domain.calendar.dto.DateDto;
import com.example.kare.domain.calendar.service.CalendarService;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TodayService {
    private final MmrRoutnMgtRepo mmrRoutnMgtRepo;
    private final MmrRoutnDtlMgtRepo mmrRoutnDtlMgtRepo;
    private final MmrRoutnAchHisRepo mmrRoutnAchHisRepo;
    private final CalendarService calculator;

    public Map<String, Object> findTodayRoutines(String memberId, LocalDate searchDate) {
        List<RoutineResDto> routineList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        //TODO : 대상건이 없을 때 처리 방법
        List<RoutineResDto> todayRoutineList = mmrRoutnMgtRepo.findTodayRoutnList(memberId, searchDate);

        // 이번주 시작일, 종료일 추출
        DateDto weekCriteria = calculator.getWeekCriteria(searchDate);

        // 루틴Sequence 기준 Map 생성 - 루틴 시퀀스로 RoutineResDto 추출하기 위함
        Map<Integer, RoutineResDto> todayRoutineMap = new HashMap<>();
        todayRoutineList.forEach(routineResDto -> todayRoutineMap.put(routineResDto.getRoutineSequence(), routineResDto));

        // 목표일 추출 로직
        calculateTargetDaysNum(memberId, todayRoutineMap, weekCriteria);

        // 달성일 추출 로직
        calculateCompleteDaysNum(memberId, todayRoutineMap, weekCriteria);

        // Grouping
        // key : 루틴그룹Sequence value : 루틴그룹에 포함된 루틴 List
        Map<Integer, List<RoutineResDto>> routineMapByRoutineGroup = new HashMap<>();
        Set<RoutineGroupResDto> routineGroupSet = new HashSet<>();

        todayRoutineList.forEach(routine -> {
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
    private void calculateTargetDaysNum(String memberId, Map<Integer, RoutineResDto> todayRoutineMap, DateDto weekCriteria) {
        // 목표일 추출 로직
        // 시작일 ~ 종료일에 해당하는 루틴 상세 추출
        List<MmrRoutnDtlMgt> routineDetailList = mmrRoutnDtlMgtRepo.findValidRoutnDtlList(
                memberId,
                weekCriteria.getStartDate(),
                weekCriteria.getEndDate(),
                todayRoutineMap.keySet()
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

            int targetDaysNum = routineDetail.getTargetDaysNum(startCriteria, endCriteria);

            RoutineResDto routineResDto = todayRoutineMap.get(routineDetail.getRoutnSeq());
            routineResDto.setTargetDaysNumPerWeek(routineResDto.getTargetDaysNumPerWeek() + targetDaysNum);

            nextRoutineSeq = routineDetail.getRoutnSeq();
            nextRoutineDetailChangeDate = routineDetail.getRoutnChDt();
        }
    }

    private void calculateCompleteDaysNum(String memberId, Map<Integer, RoutineResDto> todayRoutineMap, DateDto weekCriteria) {
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
