package com.example.kare.domain.today.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RoutineGroupResponseDto {
    private Long routineGroupId;
    private String routineGroupName;
    private List<RoutineResponseDto> routines = new ArrayList<>();
    private Integer totalCount;
    private Integer completeCount;

    public RoutineGroupResponseDto(Long routineGroupId, String routineGroupName) {
        this.routineGroupId = routineGroupId;
        this.routineGroupName = routineGroupName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoutineGroupResponseDto that = (RoutineGroupResponseDto) o;
        return Objects.equals(routineGroupId, that.routineGroupId) && Objects.equals(routineGroupName, that.routineGroupName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routineGroupId, routineGroupName);
    }
}
