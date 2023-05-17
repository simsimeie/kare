package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnDtlMgt;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MmrRoutnDtlMgtRepoCustom {
    LocalDate findValidRoutnChDt(Integer routnSeq, String mmrId, LocalDate searchDate);
    Optional<MmrRoutnDtlMgt> findValidRoutnDtl(Integer routnSeq, String mmrId, LocalDate searchDate);
    List<MmrRoutnDtlMgt> findValidRoutnDtlList(String mmrId, LocalDate startDate, LocalDate LastDate, Set<Integer> routnSeqSet);

}
