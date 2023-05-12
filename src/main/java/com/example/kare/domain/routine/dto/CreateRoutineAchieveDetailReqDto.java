package com.example.kare.domain.routine.dto;

import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.value.Goal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CreateRoutineAchieveDetailReqDto {
    private Integer routineSequence;
    private String memberId;
    @PastOrPresent
    private LocalDate requestDate;
    private GoalDto goal;

}
