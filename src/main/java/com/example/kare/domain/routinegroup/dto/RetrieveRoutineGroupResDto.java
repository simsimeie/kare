package com.example.kare.domain.routinegroup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RetrieveRoutineGroupResDto {
    private List<CommonRoutineGroupResDto> detailResDtoList = new ArrayList<>();

}
