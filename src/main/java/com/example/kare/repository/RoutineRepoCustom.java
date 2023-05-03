package com.example.kare.repository;

import com.example.kare.domain.today.dto.RoutineResponseDto;

import java.time.LocalDate;
import java.util.List;

public interface RoutineRepoCustom {
    Integer findActiveRoutineNum(String mmrId);
    List<RoutineResponseDto> findFutureRoutines(String mmrId, LocalDate searchDate);
}
