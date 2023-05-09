package com.example.kare.domain.today.controller;


import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.today.dto.RoutineGroupReqDto;
import com.example.kare.domain.today.service.RoutineGroupService;
import com.example.kare.entity.routine.id.MmrRoutnGrpMgtId;
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
    public ResponseDto<MmrRoutnGrpMgtId> createRoutineGroup(@RequestBody @Valid RoutineGroupReqDto requestDto){
        return ResponseDto.of(routineGroupService.inputRoutineGroup(requestDto));
    }
}
