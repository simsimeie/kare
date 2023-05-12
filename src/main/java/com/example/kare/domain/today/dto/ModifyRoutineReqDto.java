package com.example.kare.domain.today.dto;

import com.example.kare.domain.routine.dto.CycleDto;
import com.example.kare.domain.routine.dto.GoalDto;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.value.Cycle;
import com.example.kare.entity.routine.value.Goal;
import com.example.kare.entity.routine.MmrRoutnMgt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ModifyRoutineReqDto {
    @Positive
    private Integer routineSequence;
    @NotBlank
    private String routineName;
    @NotEmpty
    private String memberId;
    private Integer routineGroupSequence;
    private String routineGroupName;
    private boolean alarm;
    private LocalTime alarmTime;
    private @Valid CycleDto cycle;
    private @Valid GoalDto goal;
    @FutureOrPresent
    private LocalDate startDate;
    @FutureOrPresent
    private LocalDate endDate;

    public ModifyRoutineReqDto(
            String routineName,
            String memberId,
            Integer routineGroupSequence,
            boolean alarm,
            CycleDto cycle,
            GoalDto goal,
            LocalTime alarmTime,
            LocalDate startDate,
            LocalDate endDate) {
        this.routineName = routineName;
        this.memberId = memberId;
        this.routineGroupSequence = routineGroupSequence;
        this.alarm = alarm;
        this.cycle = cycle;
        this.goal = goal;
        this.alarmTime = alarmTime;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public MmrRoutnMgt toEntity(Member member, Integer routineSequence, Integer sortOrder) {

        Cycle cycle = this.getCycle().toEntity();
        Goal goal = this.getGoal().toEntity();

        return MmrRoutnMgt.createRoutine(
                this.getRoutineName()
                , routineSequence
                , member
                , this.isAlarm()
                , cycle
                , goal
                , this.getAlarmTime()
                , this.getStartDate()
                , this.getEndDate()
                , sortOrder
        );
    }

}
