package com.example.kare.domain.today.dto;

import com.example.kare.entity.routine.Cycle;
import com.example.kare.entity.routine.constant.CycleType;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

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
    @Max(7)
    @Min(1)
    private Integer count;

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

    public CycleDto(CycleType cycleType, Integer count) {
        this.cycleType = cycleType;
        this.count = count;
    }

    public Cycle toEntity(){
        return Cycle.createCycle(this);
    }
}
