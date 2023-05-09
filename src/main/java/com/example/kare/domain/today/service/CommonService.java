package com.example.kare.domain.today.service;


import com.example.kare.entity.member.Member;
import com.example.kare.repository.MmrRoutnGrpMgtRepo;
import com.example.kare.repository.MmrRoutnMgtRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CommonService {

    private final MmrRoutnGrpMgtRepo routineGroupRepo;
    private final MmrRoutnMgtRepo mmrRoutnMgtRepo;

    public Integer findMinSoOrd(Member member) {
        Integer routineSortOrderMinValue = mmrRoutnMgtRepo.findMinSoOrd(member);
        Integer routineGroupSortOrderMinValue = routineGroupRepo.findMinSoOrd(member);

        log.debug("routineSortOrderMinValue : {} , routineGroupSortOrderMinValue : {} ", routineSortOrderMinValue, routineGroupSortOrderMinValue);

        Integer minSoOrd = routineSortOrderMinValue > routineGroupSortOrderMinValue ? routineGroupSortOrderMinValue : routineSortOrderMinValue;

        log.debug("minSortOrder : {} ", minSoOrd);

        return minSoOrd;
    }
}
