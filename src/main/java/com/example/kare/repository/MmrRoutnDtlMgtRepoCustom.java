package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.querydsl.core.Tuple;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MmrRoutnDtlMgtRepoCustom {
    LocalDate findValidRoutineChangeDate(Integer routnSeq, String mmrId, LocalDate searchDate);
    Optional<MmrRoutnDtlMgt> findValidRoutineDetail(Integer routnSeq, String mmrId, LocalDate searchDate);
    List<MmrRoutnDtlMgt> findValidRoutineDetailList(String mmrId, LocalDate startDate, LocalDate LastDate, Set<Integer> routnSeqSet);

}
