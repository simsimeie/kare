package com.example.kare.repository;

import java.time.LocalDate;

public interface RoutineDetailRepoCustom {
    LocalDate findRoutineChangeDate(Integer routnSeq, String mmrId, LocalDate searchDate);
}
