package com.example.kare.domain.today.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.domain.today.service.TodayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class TodayController {
    private final TodayService todayService;

    @PostMapping("/today/routine")
    public ResponseDto<Long> createRoutine(@RequestBody @Valid RoutineRequestDto requestDto){
        return ResponseDto.of(todayService.createRoutine(requestDto));
    }
}
