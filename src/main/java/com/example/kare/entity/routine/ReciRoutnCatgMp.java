package com.example.kare.entity.routine;


import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.routine.id.ReciRoutnCatgMpId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
@IdClass(ReciRoutnCatgMpId.class)
public class ReciRoutnCatgMp extends BaseTimeEntity {
    @Id
    @Column(name="RECI_ROUTN_CATG_SEQ")
    private Integer reciRoutnCatgSeq;
    @Id
    @Column(name="RECI_ROUTN_SEQ")
    private Integer reciRoutnSeq;
    private Integer soOrd;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECI_ROUTN_CATG_SEQ", insertable = false, updatable = false)
    private ReciRoutnCatgMgt reciRoutnCatgMgt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "RECI_ROUTN_SEQ", insertable = false, updatable = false)
    private ReciRoutnMgt reciRoutnMgt;

    // ******** 생성 함수 ********
    public static ReciRoutnCatgMp createReciRoutnCatgMp(Integer reciRoutnCatgSeq, Integer reciRoutnSeq, Integer soOrd) {
        ReciRoutnCatgMp reciRoutnCatgMp = new ReciRoutnCatgMp();
        reciRoutnCatgMp.setReciRoutnCatgSeq(reciRoutnCatgSeq);
        reciRoutnCatgMp.setReciRoutnSeq(reciRoutnSeq);
        reciRoutnCatgMp.setSoOrd(soOrd);
        return reciRoutnCatgMp;
    }
}
