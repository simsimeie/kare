package com.example.kare.domain.today.controller;

import com.example.kare.domain.today.dto.CreateRoutineRequestDto;
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
    public Long createRoutine(@RequestBody @Valid CreateRoutineRequestDto requestDto){
        return todayService.createRoutine(requestDto);
    }
}
