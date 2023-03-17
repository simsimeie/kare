package com.example.kare.mapper;

import com.example.kare.domain.today.dto.RoutineResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface RoutineMapper {
    List<RoutineResponseDto> findTodayRoutines(String memberId, LocalDate searchDate, String dayOfWeek);

}
