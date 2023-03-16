package com.example.kare.domain.today.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.today.dto.RoutineGroupRequestDto;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.domain.today.service.TodayService;
import com.example.kare.domain.today.dto.LinkRoutineGroupRequestDto;
import com.example.kare.entity.routine.RoutineHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TodayController {
    private final TodayService todayService;

    @PostMapping("/today/routine")
    public ResponseDto<Long> createRoutine(@RequestBody @Valid RoutineRequestDto requestDto){
        return ResponseDto.of(todayService.createRoutine(requestDto));
    }

    @PutMapping("/today/routine")
    public ResponseDto<Long> modifyRoutine(@RequestBody @Valid RoutineRequestDto requestDto){
        return ResponseDto.of(todayService.modifyRoutine(requestDto));
    }

    @PostMapping("/today/routine-group")
    public ResponseDto<Long> createRoutineGroup(@RequestBody @Valid RoutineGroupRequestDto requestDto){
        return ResponseDto.of(todayService.createRoutineGroup(requestDto));
    }

    @PostMapping("/today/link/routine-group")
    public ResponseDto<Long> createLinkRoutineGroup(@RequestBody @Valid LinkRoutineGroupRequestDto requestDto){
        return ResponseDto.of(todayService.linkRoutineAndRoutineGroup(requestDto));
    }
}
