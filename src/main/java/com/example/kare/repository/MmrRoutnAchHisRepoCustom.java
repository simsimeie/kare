package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnAhvHis;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface MmrRoutnAchHisRepoCustom {
    List<MmrRoutnAhvHis> findCompletedAchievementList(String mmrId, LocalDate startDate, LocalDate endDate, Set<Integer> routnSeqSet);
}
