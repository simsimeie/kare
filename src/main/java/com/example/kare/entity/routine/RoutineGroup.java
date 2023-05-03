package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.member.Member;
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
@IdClass(RoutineGroupId.class)
@Table(name="MMR_ROUTN_GRP_MGT")
public class RoutineGroup extends BaseTimeEntity implements Persistable<RoutineGroupId> {
    @Id
    @Column(name="ROUTN_GRP_SEQ")
    private Integer routnGrpSeq;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="MMR_ID")
    private Member member;
    private String routnGrpNm;
    @OneToMany(mappedBy = "routineGroup")
    private List<Routine> routines = new ArrayList<>();
    private Integer soOdr;

    // ******** 복합키 관련 처리 부분 ********
    @Transient
    private boolean isNew = true;
    @Override
    public RoutineGroupId getId() {
        return new RoutineGroupId(this.getRoutnGrpSeq(), this.getMember().getId());
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
    public static RoutineGroup createRoutineGroup(Member member, String routnGrpNm, Integer routnGrpSeq, Integer soOdr){
        RoutineGroup routineGroup = new RoutineGroup();

        routineGroup.setRoutnGrpSeq(routnGrpSeq);
        routineGroup.setMember(member);
        routineGroup.setRoutnGrpNm(routnGrpNm);
        routineGroup.setSoOdr(soOdr);
        return routineGroup;
    }

}
