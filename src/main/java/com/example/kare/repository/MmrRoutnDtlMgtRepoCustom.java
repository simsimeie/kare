package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnDtlMgt;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MmrRoutnDtlMgtRepoCustom {
    LocalDate findValidRoutnChDt(String mmrId, Integer routnSeq, LocalDate searchDate);
    Optional<MmrRoutnDtlMgt> findValidRoutnDtl(String mmrId, Integer routnSeq, LocalDate searchDate);
    List<MmrRoutnDtlMgt> findValidRoutnDtlList(String mmrId, LocalDate startDate, LocalDate endDate, Set<Integer> routnSeqSet);



}
