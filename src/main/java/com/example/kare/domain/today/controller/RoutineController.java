package com.example.kare.domain.today.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.today.dto.CreateRoutineRequestDto;
import com.example.kare.domain.today.dto.RoutineGroupRequestDto;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.domain.today.service.RoutineService;
import com.example.kare.domain.today.dto.LinkRoutineGroupRequestDto;
import com.example.kare.entity.routine.RoutineGroupId;
import com.example.kare.entity.routine.RoutineId;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routine/")
public class RoutineController {
    private final RoutineService routineService;


    @PostMapping("create")
    public ResponseDto<Integer> createRoutine(@RequestBody @Valid CreateRoutineRequestDto reqDto){
        return ResponseDto.of(routineService.createRoutine(reqDto));
    }

    @PutMapping("/today/routine")
    public ResponseDto<RoutineId> modifyRoutine(@RequestBody @Valid RoutineRequestDto requestDto){
        return ResponseDto.of(routineService.modifyRoutine(requestDto));
    }



}
