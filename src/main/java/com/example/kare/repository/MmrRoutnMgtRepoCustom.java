package com.example.kare.repository;

import com.example.kare.domain.today.dto.RoutineResDto;

import java.time.LocalDate;
import java.util.List;

public interface MmrRoutnMgtRepoCustom {
    Integer findActiveRoutnNum(String mmrId);
    List<RoutineResDto> findTodayRoutnList(String mmrId, LocalDate searchDate);
}
