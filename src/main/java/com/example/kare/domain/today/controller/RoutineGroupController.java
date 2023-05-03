package com.example.kare.domain.today.controller;


import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.today.dto.RoutineGroupRequestDto;
import com.example.kare.domain.today.service.RoutineGroupService;
import com.example.kare.entity.routine.RoutineGroupId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routine-group/")
public class RoutineGroupController {
    private final RoutineGroupService routineGroupService;
    @PostMapping("/create")
    public ResponseDto<RoutineGroupId> createRoutineGroup(@RequestBody @Valid RoutineGroupRequestDto requestDto){
        return ResponseDto.of(routineGroupService.inputRoutineGroup(requestDto));
    }
}
