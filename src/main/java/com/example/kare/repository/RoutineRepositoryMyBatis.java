package com.example.kare.repository;

import com.example.kare.domain.today.dto.RoutineResponseDto;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

public interface RoutineRepositoryMyBatis {
    List<RoutineResponseDto> findFutureRoutines(String memberId, LocalDate searchDate);
}
