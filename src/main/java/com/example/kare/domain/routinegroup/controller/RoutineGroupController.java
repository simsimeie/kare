package com.example.kare.domain.routinegroup.controller;


import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.routinegroup.dto.*;
import com.example.kare.domain.routinegroup.service.RoutineGroupService;
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
    public ResponseDto<CommonRoutineGroupResDto> createRoutineGroup(@RequestBody @Valid CreateRoutineGroupReqDto reqDto){
        return ResponseDto.of(routineGroupService.inputRoutineGroup(reqDto));
    }

    @PostMapping("/select")
    public ResponseDto<RetrieveRoutineGroupResDto> retrieveRoutineGroupByMember(@RequestBody @Valid RetrieveRoutineGroupReqDto reqDto){
        return ResponseDto.of(routineGroupService.findRoutineGroupByMember(reqDto));
    }

    @PostMapping("/update")
    public ResponseDto<CommonRoutineGroupResDto> updateRoutineGroup(@RequestBody @Valid UpdateRoutineGroupReqDto reqDto){
        return ResponseDto.of(routineGroupService.modifyRoutineGroup(reqDto));
    }

    @PostMapping("/delete")
    public ResponseDto<Void> deleteRoutineGroup(@RequestBody @Valid DeleteRoutineGroupReqDto reqDto){
        routineGroupService.removeRoutineGroup(reqDto);
        return ResponseDto.of(null);
    }


}
