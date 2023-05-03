package com.example.kare.entity.routine;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class RoutineDetailId implements Serializable {
    private LocalDate routnChDt;
    private Integer routnSeq;
    private String mmrId;

    public RoutineDetailId(LocalDate routnChDt, Integer routnSeq, String mmrId) {
        this.routnChDt = routnChDt;
        this.routnSeq = routnSeq;
        this.mmrId = mmrId;
    }
}
