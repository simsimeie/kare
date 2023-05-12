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
    private CycleType cycleType;
    private boolean mon;
    private boolean tue;
    private boolean wed;
    private boolean thu;
    private boolean fri;
    private boolean sat;
    private boolean sun;
    @Max(value = 7, message = "주별 회수의 최대 값은 7 입니다.")
    @Min(value = 1, message = "주별 회수의 최소 값은 1 입니다.")
    private Integer cycleCount;

    public CycleDto(CycleType cycleType, boolean mon, boolean tue, boolean wed, boolean thu, boolean fri, boolean sat, boolean sun) {
        this.cycleType = cycleType;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }

    public CycleDto(CycleType cycleType, Integer cycleCount) {
        this.cycleType = cycleType;
        this.cycleCount = cycleCount;
    }

    public Cycle toEntity(){
        return Cycle.createCycle(this);
    }
}
