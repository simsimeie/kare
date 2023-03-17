package com.example.kare.repository;

import com.example.kare.domain.today.dto.RoutineResponseDto;
import com.example.kare.mapper.RoutineMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoutineRepositoryMyBatisImpl implements RoutineRepositoryMyBatis{
    private final EntityManager em;
    private final RoutineMapper mapper;
    @Override
    public List<RoutineResponseDto> findFutureRoutines(String memberId, LocalDate searchDate) {
        DayOfWeek dayOfWeek = searchDate.getDayOfWeek();
        return mapper.findTodayRoutines(memberId, searchDate, dayOfWeek.toString());
    }
}
