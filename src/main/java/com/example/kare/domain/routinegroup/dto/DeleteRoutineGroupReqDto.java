package com.example.kare.domain.routinegroup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class DeleteRoutineGroupReqDto {
    private String memberId;
    private Integer routineGroupSequence;
}
