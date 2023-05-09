package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.routine.id.MmrRoutnAhvHisId;
import com.example.kare.entity.routine.id.MmrRoutnDtlMgtId;
import com.example.kare.entity.routine.id.MmrRoutnGrpMgtId;
import com.example.kare.entity.routine.value.Goal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
@IdClass(MmrRoutnAhvHisId.class)
public class MmrRoutnAhvHis extends BaseTimeEntity implements Persistable<MmrRoutnAhvHisId> {
    @Id
    @Column(name="ROUTN_AHV_DT")
    private LocalDate routnAhvDt;
    @Id
    @Column(name="ROUTN_CH_DT")
    private LocalDate routnChDt;
    @Id
    @Column(name="ROUTN_SEQ")
    private Integer routnSeq;
    @Id
    @Column(name="MMR_ID")
    private String mmrId;
    @Embedded
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "ROUTN_CH_DT", referencedColumnName = "ROUTN_CH_DT", insertable = false, updatable = false),
            @JoinColumn(name = "ROUTN_SEQ", referencedColumnName = "ROUTN_SEQ", insertable = false, updatable = false),
            @JoinColumn(name = "MMR_ID", referencedColumnName = "MMR_ID", insertable = false, updatable = false)
    })
    private MmrRoutnDtlMgt mmrRoutnDtlMgt;


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
    public MmrRoutnAhvHisId getId() {
        return new MmrRoutnAhvHisId(this.routnAhvDt, this.routnChDt, this.getRoutnSeq(), this.getMmrId());
    }
    @Override
    public boolean isNew() {
        return this.isNew;
    }

    // ******** 생성 함수 ********
    public static MmrRoutnAhvHis createRoutineAchievement(
            LocalDate routnAhvDt,
            LocalDate routnChDt,
            Integer routnSeq,
            String mmrId,
            Goal goal
    ){
        MmrRoutnAhvHis achievement = new MmrRoutnAhvHis();
        achievement.setRoutnAhvDt(routnAhvDt);
        achievement.setRoutnChDt(routnChDt);
        achievement.setRoutnSeq(routnSeq);
        achievement.setMmrId(mmrId);
        achievement.setGoal(goal);

        return achievement;
    }
}
