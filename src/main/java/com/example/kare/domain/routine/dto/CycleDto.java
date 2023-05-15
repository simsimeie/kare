package com.example.kare.domain.routine.dto;

import com.example.kare.entity.routine.value.Cycle;
import com.example.kare.entity.routine.constant.CycleType;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CycleDto {
    private CycleType repeatCycleTypeCode;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
    @Max(value = 7, message = "주별 회수의 최대 값은 7 입니다.")
    @Min(value = 1, message = "주별 회수의 최소 값은 1 입니다.")
    private Integer repeatCycleNum;

    public CycleDto(CycleType repeatCycleTypeCode, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday) {
        this.repeatCycleTypeCode = repeatCycleTypeCode;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
    }

    public CycleDto(CycleType repeatCycleTypeCode, Integer repeatCycleNum) {
        this.repeatCycleTypeCode = repeatCycleTypeCode;
        this.repeatCycleNum = repeatCycleNum;
    }

    public Cycle toEntity(){
        return Cycle.createCycle(this);
    }
}
