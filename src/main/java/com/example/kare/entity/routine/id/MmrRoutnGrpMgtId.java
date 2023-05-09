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
public class MmrRoutnGrpMgtId implements Serializable {
    private Integer routnGrpSeq;
    private String member;
    
    public MmrRoutnGrpMgtId(Integer routnGrpSeq, String member) {
        this.routnGrpSeq = routnGrpSeq;
        this.member = member;
    }
}
