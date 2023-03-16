package com.example.kare.repository;

import com.example.kare.entity.routine.Routine;
import com.example.kare.entity.routine.RoutineHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RoutineHistoryRepository extends JpaRepository<RoutineHistory, Long> {

    RoutineHistory findLastHistoryByRoutineAndEndDate(Routine routine, LocalDate endDate);
}
