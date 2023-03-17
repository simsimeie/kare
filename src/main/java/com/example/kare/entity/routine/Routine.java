package com.example.kare.entity.routine;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
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
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="link_routine_group_id")
    private LinkRoutineGroup linkRoutineGroup;
    @OneToMany(mappedBy = "routine", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<RoutineHistory> routineHistory = new ArrayList<>();
    private boolean alarm;
    @Embedded
    private Cycle cycle;
    @Embedded
    private Goal goal;
    private LocalTime alarmTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer displayOrder;


    // ******** 생성 함수 ********
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

        // 서비스에서 묶을지 엔티티에서 묶을지 고민
        // 2년 지난 데이터를 삭제한다면, 양방향 연관관계로 설정 -> 엔티티에 묶어도 상관 없다. 덕분에 생성은 아래와 같이 객체에 추가하는 것만으로도 가능 삭제는 루틴이 삭제되면 알아서 삭제되는 기능 사용 가능
        // 2년 지난 데이터를 삭제하지 않는다면, 단방향 연관관계로 설정 -> 서비스에 묶어야 한다. 생성, 수정, 삭제 모두 기능 개발해서 서비스에서 묶어줘야 한다.
        RoutineHistory history = RoutineHistory.createRoutineHistory(routine, startDate);
        routine.getRoutineHistory().add(history);

        return routine;
    }

    // ******** 비즈니스 로직 ********
    public void addRoutineToGroup(RoutineGroup group){
        LinkRoutineGroup linkRoutineGroup = LinkRoutineGroup.createLinkRoutineGroup(this, group);
        this.setLinkRoutineGroup(linkRoutineGroup);
        group.getLinkRoutineGroups().add(linkRoutineGroup);
    }
    public void clearRoutineGroup(){
        this.setLinkRoutineGroup(null);
    }

    public Routine modifyRoutine(RoutineRequestDto toBe){

        if(Period.between(LocalDate.now(), toBe.getStartDate()).isNegative()
                && !this.getStartDate().equals(toBe.getStartDate())){
            throw new KBException("루틴의 시작 일자를 과거로 설정할 수 없습니다.", ErrorCode.BAD_REQUEST);
        }

        if(Period.between(LocalDate.now(), this.getStartDate()).isNegative()
                && !this.getStartDate().equals(toBe.getStartDate())){
            throw new KBException("이미 시작된 루틴의 시작일은 변경할 수 없습니다.", ErrorCode.BAD_REQUEST);
        }

        Cycle toBeCycle = toBe.getCycle().toEntity();
        Goal toBeGoal = toBe.getGoal().toEntity();

        this.setName(toBe.getName());
        this.setAlarm(toBe.isAlarm());
        this.setCycle(toBeCycle);
        this.setGoal(toBeGoal);
        this.setAlarmTime(toBe.getAlarmTime());
        this.setStartDate(toBe.getStartDate());
        this.setEndDate(toBe.getEndDate());

        return this;
    }



}
