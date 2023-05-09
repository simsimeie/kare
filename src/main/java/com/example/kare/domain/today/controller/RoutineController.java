package com.example.kare.domain.today.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.today.dto.CreateRoutineReqDto;
import com.example.kare.domain.today.dto.ModifyRoutineReqDto;
import com.example.kare.domain.today.service.RoutineService;
import com.example.kare.entity.routine.id.MmrRoutnMgtId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routine")
public class RoutineController {
    private final RoutineService routineService;

    @PostMapping("/create")
    public ResponseDto<Integer> createRoutine(@RequestBody @Valid CreateRoutineReqDto reqDto){
        return ResponseDto.of(routineService.inputRoutine(reqDto));
    }

    @PutMapping("/update")
    public ResponseDto<MmrRoutnMgtId> modifyRoutine(@RequestBody @Valid ModifyRoutineReqDto reqDto){
        return ResponseDto.of(routineService.modifyRoutine(reqDto));
    }



}
