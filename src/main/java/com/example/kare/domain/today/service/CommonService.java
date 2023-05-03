package com.example.kare.domain.today.service;


import com.example.kare.entity.member.Member;
import com.example.kare.repository.RoutineGroupRepo;
import com.example.kare.repository.RoutineRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommonService {

    private final RoutineGroupRepo routineGroupRepo;
    private final RoutineRepo routineRepo;

    public Integer findMinSortOrder(Member member) {
        Integer routineSortOrderMinValue = routineRepo.findMinSortOrder(member);
        Integer routineGroupSortOrderMinValue = routineGroupRepo.findMinSortOrder(member);

        log.debug("routineSortOrderMinValue : {} , routineGroupSortOrderMinValue : {} ", routineSortOrderMinValue, routineGroupSortOrderMinValue);

        Integer minSortOrder = routineSortOrderMinValue > routineGroupSortOrderMinValue ? routineGroupSortOrderMinValue : routineSortOrderMinValue;

        log.debug("minSortOrder : {} ", minSortOrder);

        return minSortOrder;
    }
}
