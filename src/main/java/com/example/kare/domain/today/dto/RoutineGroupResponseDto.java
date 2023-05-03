package com.example.kare.domain.today.dto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@EqualsAndHashCode
public class RoutineGroupResponseDto {
    private Integer routineGroupSequence;
    private String routineGroupName;
    private List<RoutineResponseDto> routines = new ArrayList<>();
    private Integer totalCount;
    private Integer completeCount;

    public RoutineGroupResponseDto(Integer routineGroupSequence, String routineGroupName) {
        this.routineGroupSequence = routineGroupSequence;
        this.routineGroupName = routineGroupName;
    }

}
