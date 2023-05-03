package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
@IdClass(RoutineDetailId.class)
@Table(name="MMR_ROUTN_DTL_MGT")
public class RoutineDetail extends BaseTimeEntity implements Persistable<RoutineDetailId> {
    @Id
    @Column(name="ROUTN_CH_DT")
    private LocalDate routnChDt;
    @Id
    @Column(name="ROUTN_SEQ")
    private Integer routnSeq;
    @Id
    @Column(name="MMR_ID")
    private String mmrId;

    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumns로 굳이 묶지 않아도 된다.
    @JoinColumns({
        @JoinColumn(name = "ROUTN_SEQ", referencedColumnName = "ROUTN_SEQ", insertable = false, updatable = false),
        @JoinColumn(name = "MMR_ID", referencedColumnName = "MMR_ID", insertable = false, updatable = false)
    })
    private Routine routine;
    private String routnNm;
    private boolean ntfYn;
    private LocalTime ntfTi;
    @Embedded
    private Cycle cycle;
    @Embedded
    private Goal goal;

    private LocalDate startDate;
    private LocalDate endDate;


    // ******** 복합키 관련 처리 부분 ********
    @Transient
    private boolean isNew = true;
    @PrePersist
    public void generateKey(){
        this.routnChDt = LocalDate.now();
        this.isNew = false;
    }
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }

    @Override
    public RoutineDetailId getId() {
        return new RoutineDetailId(this.routnChDt, this.getRoutnSeq(), this.getMmrId());
    }
    @Override
    public boolean isNew() {
        return this.isNew;
    }



    // ******** 생성 함수 ********
    public static RoutineDetail createRoutineHistory(Routine routine, LocalDate startDate){
        RoutineDetail routineHistory = new RoutineDetail();

        routineHistory.setRoutnChDt(LocalDate.now());
        routineHistory.setRoutnSeq(routine.getRoutnSeq());
        routineHistory.setMmrId(routine.getMember().getId());
        routineHistory.setCycle(routine.getCycle());
        routineHistory.setGoal(routine.getGoal());
        routineHistory.setStartDate(startDate);
        routineHistory.setEndDate(routine.getEndDate());

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

    public void modifyRoutineCharacter(Routine toBe){
        this.setCycle(toBe.getCycle());
        this.setGoal(toBe.getGoal());
    }

    public void modifyRoutineStartDate(LocalDate startDate){
        this.setStartDate(startDate);
    }

    public void modifyRoutineHistoryEndDate(LocalDate endDate){
        this.setEndDate(endDate);
    }


}
