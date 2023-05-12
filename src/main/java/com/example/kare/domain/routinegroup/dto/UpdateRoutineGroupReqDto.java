package com.example.kare.domain.routinegroup.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class UpdateRoutineGroupReqDto {
    private String memberId;
    private Integer routineGroupSequence;
    private String routineGroupName;
}
