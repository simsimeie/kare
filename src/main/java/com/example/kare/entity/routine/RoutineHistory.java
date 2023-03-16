package com.example.kare.entity.routine;

import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
public class RoutineHistory extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name="routine_history_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id")
    private Routine routine;
    @Embedded
    private Cycle cycle;
    @Embedded
    private Goal goal;
    private LocalDate startDate;
    private LocalDate endDate;


    // ******** 생성 함수 ********
    public static RoutineHistory createRoutineHistory(Routine routine, LocalDate startDate){
        RoutineHistory routineHistory = new RoutineHistory();
        routineHistory.setRoutine(routine);
        routineHistory.setCycle(routine.getCycle());
        routineHistory.setGoal(routine.getGoal());
        routineHistory.setStartDate(startDate);
        routineHistory.setEndDate(LocalDate.of(9999,12,31));

        return routineHistory;
    }

    // ******** 비즈니스 로직 ********
    public boolean isShouldUpdateRoutineHistory(Routine toBe){
        if(! this.getCycle().equals(toBe.getCycle())){
            return true;
        }

        if(! this.getGoal().equals(toBe.getGoal())){
            return true;
        }

        if(! this.getStartDate().equals(toBe.getStartDate())){
            return true;
        }

        return false;
    }

    public void modifyRoutineHistory(Routine toBe){
        this.setCycle(toBe.getCycle());
        this.setGoal(toBe.getGoal());
        this.setStartDate(toBe.getStartDate());
    }

    public void changeRoutineHistoryEndDate(LocalDate endDate){
        this.setEndDate(endDate);
    }
}
