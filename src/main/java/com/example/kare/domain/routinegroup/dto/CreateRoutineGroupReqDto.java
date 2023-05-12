package com.example.kare.domain.routinegroup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CreateRoutineGroupReqDto {
    @NotEmpty
    private String memberId;
    @NotBlank
    private String routineGroupName;

}
