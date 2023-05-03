package com.example.kare.domain.today.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.today.dto.CreateRoutineRequestDto;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.domain.today.service.RoutineService;
import com.example.kare.entity.routine.RoutineId;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routine")
public class RoutineController {
    private final RoutineService routineService;

    @PostMapping("/create")
    public ResponseDto<Integer> createRoutine(@RequestBody @Valid CreateRoutineRequestDto reqDto){
        return ResponseDto.of(routineService.inputRoutine(reqDto));
    }

    @PutMapping("/update")
    public ResponseDto<RoutineId> modifyRoutine(@RequestBody @Valid RoutineRequestDto reqDto){
        return ResponseDto.of(routineService.modifyRoutine(reqDto));
    }



}
