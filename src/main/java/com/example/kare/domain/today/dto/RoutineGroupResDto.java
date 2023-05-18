package com.example.kare.domain.today.dto;


import com.example.kare.domain.today.dto.RoutineResDto;
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
    private List<RoutineResDto> routineList = new ArrayList<>();


    public RoutineGroupResDto(RoutineResDto routineResDto) {
        this.routineGroupSequence = routineResDto.getRoutineGroupSequence();
        this.routineGroupName = routineResDto.getRoutineName();
        this.sortOrder = routineResDto.getRoutineGroupSortOrder();
    }

}
