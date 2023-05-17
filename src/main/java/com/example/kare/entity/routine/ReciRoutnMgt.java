package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.routine.value.Cycle;
import com.example.kare.entity.routine.value.Goal;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
public class ReciRoutnMgt extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Integer reciRoutnSeq;
    private String routnNm;
    private String reciRoutnDsc;
    private String ntfYn;
    private LocalTime ntfTi;
    private Cycle repeatCycle;
    private Goal goal;
    private LocalDate stDt;
    private LocalDate enDt;

    // ******** 생성 함수 ********
    public static ReciRoutnMgt createReciRoutnMgt(
            String routnNm,
            String reciRoutnDsc,
            String ntfYn,
            LocalTime ntfTi,
            Cycle repeatCycle,
            Goal goal,
            LocalDate stDt,
            LocalDate enDt
    ) {
        ReciRoutnMgt reciRoutnMgt = new ReciRoutnMgt();
        reciRoutnMgt.setRoutnNm(routnNm);
        reciRoutnMgt.setRoutnNm(reciRoutnDsc);
        reciRoutnMgt.setNtfYn(ntfYn);
        reciRoutnMgt.setNtfTi(ntfTi);
        reciRoutnMgt.setRepeatCycle(repeatCycle);
        reciRoutnMgt.setGoal(goal);
        reciRoutnMgt.setStDt(stDt);
        reciRoutnMgt.setEnDt(enDt);

        return reciRoutnMgt;
    }

}
