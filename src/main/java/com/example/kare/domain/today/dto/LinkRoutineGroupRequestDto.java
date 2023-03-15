package com.example.kare.domain.today.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class LinkRoutineGroupRequestDto {
    @Positive
    private Long routineId;
    @Nullable
    private Long routineGroupId;
}
