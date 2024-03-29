package com.example.kare.entity.routine.id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class MmrRoutnMgtId implements Serializable {
    private Integer routnSeq;
    private String member;

    public MmrRoutnMgtId(Integer routnSeq, String member) {
        this.routnSeq = routnSeq;
        this.member = member;
    }

}
