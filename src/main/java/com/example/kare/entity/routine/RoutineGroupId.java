package com.example.kare.entity.routine;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
public class RoutineGroupId implements Serializable {
    private Integer routnGrpSeq;
    private String member;

    public RoutineGroupId() {
    }

    public RoutineGroupId(Integer routnGrpSeq, String member) {
        this.routnGrpSeq = routnGrpSeq;
        this.member = member;
    }
}
