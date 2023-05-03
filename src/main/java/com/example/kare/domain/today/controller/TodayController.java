package com.example.kare.domain.today.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.today.dto.TodaySearchRequestDto;
import com.example.kare.domain.today.service.RoutineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/today/")
public class TodayController {
    private final RoutineService routineService;

    @PostMapping("/select")
    public ResponseDto<Map<String, Object>> retrieveRoutine(@RequestBody @Valid TodaySearchRequestDto reqDto) {
        return ResponseDto.of(
                routineService.retrieveRoutine(
                        reqDto.getMemberId(),
                        reqDto.getSearchDate())
        );
    }
}
