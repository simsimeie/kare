package com.example.kare.domain.today.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@EqualsAndHashCode
public class RoutineGroupResDto {
    private Integer routineGroupSequence;
    private String routineGroupName;
    private Integer totalRoutineNum;
    private Integer completeRoutineNum;
    private boolean routineGroupCompleteStatus;
    private Integer sortOrder;
    private List<RoutineResDto> routines = new ArrayList<>();


    public RoutineGroupResDto(Integer routineGroupSequence, String routineGroupName) {
        this.routineGroupSequence = routineGroupSequence;
        this.routineGroupName = routineGroupName;
    }

}
