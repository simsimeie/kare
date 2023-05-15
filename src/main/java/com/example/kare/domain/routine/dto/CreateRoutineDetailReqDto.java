package com.example.kare.domain.routine.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CreateRoutineDetailReqDto {
    @NotBlank
    private String routineName;
    @NotEmpty
    private String memberId;
    private Integer routineGroupSequence;
    private String routineGroupName;
    private String notificationStatus;
    private LocalTime notificationTime;
    private @Valid CycleDto repeatCycle;
    private @Valid GoalDto goal;
    @FutureOrPresent
    private LocalDate startDate;
    @FutureOrPresent
    private LocalDate endDate;

    public CreateRoutineDetailReqDto(
            String routineName,
            String memberId,
            Integer routineGroupSequence,
            String notificationStatus,
            LocalTime notificationTime,
            CycleDto repeatCycle,
            GoalDto goal,
            LocalDate startDate,
            LocalDate endDate) {
        this.routineName = routineName;
        this.memberId = memberId;
        this.routineGroupSequence = routineGroupSequence;
        this.notificationStatus = notificationStatus;
        this.notificationTime = notificationTime;
        this.repeatCycle = repeatCycle;
        this.goal = goal;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
