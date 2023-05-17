package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
public class ReciRoutnCatgMgt extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Integer reciRoutnCatgSeq;
    private String catgNm;
    private String catgDsc;
    private String catgImgFlNm;
    private Integer soOdr;

    // ******** 생성 함수 ********
    public static ReciRoutnCatgMgt createReciRoutnCatMgt(
            String catgNm,
            String catgDsc,
            String catgImgFlNm,
            Integer soOdr
    ) {
        ReciRoutnCatgMgt reciRoutnCatgMgt = new ReciRoutnCatgMgt();
        reciRoutnCatgMgt.setCatgNm(catgNm);
        reciRoutnCatgMgt.setCatgDsc(catgDsc);
        reciRoutnCatgMgt.setCatgImgFlNm(catgImgFlNm);
        reciRoutnCatgMgt.setSoOdr(soOdr);
        return reciRoutnCatgMgt;
    }
}
