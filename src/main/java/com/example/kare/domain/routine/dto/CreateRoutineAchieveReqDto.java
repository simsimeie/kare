package com.example.kare.domain.routine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CreateRoutineAchieveReqDto {
    private @Valid List<CreateRoutineAchieveDetailReqDto> routineList;


}
