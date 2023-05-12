package com.example.kare.domain.routine.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CreateRoutineReqDto {
    private @Valid List<CreateRoutineDetailReqDto> routineList;
}
