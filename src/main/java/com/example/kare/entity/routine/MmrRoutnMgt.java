package com.example.kare.entity.routine;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.ModifyRoutineReqDto;
import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.id.MmrRoutnGrpMgtId;
import com.example.kare.entity.routine.id.MmrRoutnMgtId;
import com.example.kare.entity.routine.value.Cycle;
import com.example.kare.entity.routine.value.Goal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
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
@IdClass(MmrRoutnMgtId.class)
public class MmrRoutnMgt extends BaseTimeEntity implements Persistable<MmrRoutnMgtId> {
    @Id
    @Column(name="ROUTN_SEQ")
    private Integer routnSeq;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MMR_ID")
    private Member member;
    private String routnNm;
    @Column(name="ROUTN_GRP_SEQ")
    private Integer routnGrpSeq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "ROUTN_GRP_SEQ", referencedColumnName = "ROUTN_GRP_SEQ", insertable = false, updatable = false),
        @JoinColumn(name = "MMR_ID", referencedColumnName = "MMR_ID", insertable = false, updatable = false)
    })
    private MmrRoutnGrpMgt mmrRoutnGrpMgt;
    @OneToMany(mappedBy = "mmrRoutnMgt", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<MmrRoutnDtlMgt> routineDetailList = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer soOrd;
    @Transient
    private String ntfYn;
    @Transient
    private LocalTime ntfTi;
    @Transient
    private Cycle repeatCycle;
    @Transient
    private Goal goal;

    // ******** 복합키 관련 처리 부분 ********
    @Transient
    private boolean isNew = true;
    @Override
    public MmrRoutnMgtId getId() {
        return new MmrRoutnMgtId(this.routnSeq, this.member.getId());
    }
    @Override
    public boolean isNew() {
        return isNew;
    }
    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }


    // ******** 생성 함수 ********
    public static MmrRoutnMgt createRoutine(
            String routnNm,
            Integer routnSeq,
            Member member,
            String ntfYn,
            LocalTime ntfTi,
            Cycle cycle,
            Goal goal,
            LocalDate startDate,
            LocalDate endDate,
            Integer displayOrder
    ){
        MmrRoutnMgt mmrRoutnMgt = new MmrRoutnMgt();

        mmrRoutnMgt.setRoutnNm(routnNm);
        mmrRoutnMgt.setRoutnSeq(routnSeq);
        mmrRoutnMgt.setMember(member);
        mmrRoutnMgt.setNtfYn(ntfYn);
        mmrRoutnMgt.setNtfTi(ntfTi);
        mmrRoutnMgt.setRepeatCycle(cycle);
        mmrRoutnMgt.setGoal(goal);
        mmrRoutnMgt.setStartDate(startDate);
        mmrRoutnMgt.setEndDate(endDate);
        mmrRoutnMgt.setSoOrd(Optional.ofNullable(displayOrder).orElse(1));

        // 서비스에서 묶을지 엔티티에서 묶을지 고민
        // 2년 지난 데이터를 삭제한다면, 양방향 연관관계로 설정 -> 엔티티에 묶어도 상관 없다. 덕분에 생성은 아래와 같이 객체에 추가하는 것만으로도 가능 삭제는 루틴이 삭제되면 알아서 삭제되는 기능 사용 가능
        // 2년 지난 데이터를 삭제하지 않는다면, 단방향 연관관계로 설정 -> 서비스에 묶어야 한다. 생성, 수정, 삭제 모두 기능 개발해서 서비스에서 묶어줘야 한다.
        MmrRoutnDtlMgt detail = MmrRoutnDtlMgt.createRoutineDetails(mmrRoutnMgt, startDate);
        mmrRoutnMgt.getRoutineDetailList().add(detail);

        return mmrRoutnMgt;
    }

    // ******** 비즈니스 로직 ********
    public void modifyRoutine(ModifyRoutineReqDto toBe){

//        if(Period.between(LocalDate.now(), toBe.getStartDate()).isNegative()
//                && !this.getStartDate().equals(toBe.getStartDate())){
//            throw new KBException("루틴의 시작 일자를 과거로 설정할 수 없습니다.", ErrorCode.BAD_REQUEST);
//        }

        if(Period.between(LocalDate.now(), this.getStartDate()).isNegative()
                && !this.getStartDate().equals(toBe.getStartDate())){
            throw new KBException("이미 시작된 루틴의 시작일은 변경할 수 없습니다.", ErrorCode.BAD_REQUEST);
        }

        Cycle toBeCycle = toBe.getCycle().toEntity();
        Goal toBeGoal = toBe.getGoal().toEntity();

        this.setRoutnNm(toBe.getRoutineName());
        this.setRoutnGrpSeq(toBe.getRoutineGroupSequence());
        this.setNtfYn(toBe.getNotificationStatus());
        this.setNtfTi(toBe.getNotificationTime());
        this.setRepeatCycle(toBeCycle);
        this.setGoal(toBeGoal);
        this.setStartDate(toBe.getStartDate());
        this.setEndDate(toBe.getEndDate());
    }

    public void mapToRoutineGroup(MmrRoutnGrpMgt mmrRoutnGrpMgt){
        this.setRoutnGrpSeq(mmrRoutnGrpMgt.getRoutnGrpSeq());
    }
    public void mapToRoutineGroup(MmrRoutnGrpMgtId mmrRoutnGrpMgtId){
        this.setRoutnGrpSeq(mmrRoutnGrpMgtId.getRoutnGrpSeq());
    }


}
