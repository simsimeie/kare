package com.example.kare.domain.today.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.today.dto.TodaySearchReqDto;
import com.example.kare.domain.routine.service.RoutineService;
import com.example.kare.domain.today.service.TodayService;
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
    private final TodayService todayService;

    @PostMapping("/select")
    public ResponseDto<Map<String, Object>> retrieveRoutine(@RequestBody @Valid TodaySearchReqDto reqDto) {
        return ResponseDto.of(
                todayService.findTodayRoutines(
                        reqDto.getMemberId(),
                        reqDto.getSearchDate())
        );
    }
}
