package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
public class Routine extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name="routine_id")
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private boolean alarm;
    @Embedded
    private Cycle cycle;
    @Embedded
    private Goal goal;
    private LocalTime alarmTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer displayOrder;

    public static Routine createRoutine(
            String name,
            Member member,
            boolean alarm,
            Cycle cycle,
            Goal goal,
            LocalTime alarmTime,
            LocalDate startDate,
            LocalDate endDate,
            Integer displayOrder
    ){
        Routine routine = new Routine();
        routine.setName(name);
        routine.setMember(member);
        routine.setAlarm(alarm);
        routine.setCycle(cycle);
        routine.setGoal(goal);
        routine.setAlarmTime(alarmTime);
        routine.setStartDate(startDate);
        routine.setEndDate(endDate);
        routine.setDisplayOrder(Optional.ofNullable(displayOrder).orElse(1));

        return routine;
    }

    public void setId(Long id){
        this.id = id;
    }


}
