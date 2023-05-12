package com.example.kare.domain.routinegroup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CommonRoutineGroupResDto {
    private Integer routineGroupSequence;
    private String memberId;
    private String routineGroupName;
}
