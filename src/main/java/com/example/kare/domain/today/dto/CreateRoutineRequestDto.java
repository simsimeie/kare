package com.example.kare.domain.today.dto;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Cycle;
import com.example.kare.entity.routine.Goal;
import com.example.kare.entity.routine.Routine;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CreateRoutineRequestDto {
    @NotBlank
    private String name;
    @NotEmpty
    private String memberId;
    private boolean alarm;
    private @Valid CycleDto cycle;
    private @Valid GoalDto goal;
    private LocalTime alarmTime;
    private LocalDate startDate;
    private LocalDate endDate;

    public Routine toEntity(Member member, Integer displayOrder){

        Cycle cycle = this.getCycle().toEntity();
        Goal goal = this.getGoal().toEntity();

        return Routine.createRoutine(
                this.getName()
                , member
                , this.isAlarm()
                , cycle
                , goal
                , this.getAlarmTime()
                , this.getStartDate()
                , this.getEndDate()
                , displayOrder
        );
    }

}
