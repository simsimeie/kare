package com.example.kare.domain.today.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateRoutineRequestDto {
    private @Valid List<RoutineRequestDto> routineList;
}
