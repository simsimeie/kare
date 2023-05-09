package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnDtlMgt;

import java.time.LocalDate;

public interface MmrRoutnDtlMgtRepoCustom {
    LocalDate findValidRoutineChangeDate(Integer routnSeq, String mmrId, LocalDate searchDate);
    MmrRoutnDtlMgt findValidRoutineDetail(Integer routnSeq, String mmrId, LocalDate searchDate);
}
