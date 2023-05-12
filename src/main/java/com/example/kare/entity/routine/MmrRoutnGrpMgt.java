package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.id.MmrRoutnGrpMgtId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
@IdClass(MmrRoutnGrpMgtId.class)
public class MmrRoutnGrpMgt extends BaseTimeEntity implements Persistable<MmrRoutnGrpMgtId> {
    @Id
    @Column(name="ROUTN_GRP_SEQ")
    private Integer routnGrpSeq;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MMR_ID")
    private Member member;
    private String routnGrpNm;
    @OneToMany(mappedBy = "mmrRoutnGrpMgt")
    private List<MmrRoutnMgt> mmrRoutnMgtList = new ArrayList<>();
    private Integer soOdr;

    // ******** 복합키 관련 처리 부분 ********
    @Transient
    private boolean isNew = true;
    @Override
    public MmrRoutnGrpMgtId getId() {
        return new MmrRoutnGrpMgtId(this.getRoutnGrpSeq(), this.getMember().getId());
    }
    @Override
    public boolean isNew() {
        return this.isNew;
    }
    @PrePersist
    @PostLoad
    void markNotNew() {
        this.isNew = false;
    }


    // ******** 생성 함수 ********
    public static MmrRoutnGrpMgt createRoutineGroup(Member member, String routnGrpNm, Integer routnGrpSeq, Integer soOdr){
        MmrRoutnGrpMgt mmrRoutnGrpMgt = new MmrRoutnGrpMgt();

        mmrRoutnGrpMgt.setRoutnGrpSeq(routnGrpSeq);
        mmrRoutnGrpMgt.setMember(member);
        mmrRoutnGrpMgt.setRoutnGrpNm(routnGrpNm);
        mmrRoutnGrpMgt.setSoOdr(soOdr);
        return mmrRoutnGrpMgt;
    }

    // ******** 비즈니스 로직 ********
    public void changeRoutineGroupName(String newName){
        this.setRoutnGrpNm(newName);
    }
}
