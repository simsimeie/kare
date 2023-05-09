package com.example.kare.repository;

import com.example.kare.domain.today.dto.RoutineResDto;

import java.time.LocalDate;
import java.util.List;

public interface MmrRoutnMgtRepoCustom {
    Integer findActiveRoutineNum(String mmrId);
    List<RoutineResDto> findTodayRoutines(String mmrId, LocalDate searchDate);
}
