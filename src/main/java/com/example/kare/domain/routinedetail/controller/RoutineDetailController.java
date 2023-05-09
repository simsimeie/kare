package com.example.kare.domain.routinedetail.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.routinedetail.dto.CreateRoutineAchieveReqDto;
import com.example.kare.domain.routinedetail.service.RoutineDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routine-achievement")
public class RoutineDetailController {
    private final RoutineDetailService routineDetailService;

    @PostMapping("/create")
    public ResponseDto<Void> inputRoutineAchievement(@RequestBody CreateRoutineAchieveReqDto reqDto){
        routineDetailService.inputRoutineAchievement(reqDto);
        return ResponseDto.of(null);
    }

    @DeleteMapping("/routine-detail/routine")
    public ResponseDto<Void> deleteRoutine(Integer routnSeq, String memberId ){
        routineDetailService.deleteRoutine(routnSeq, memberId);
        return ResponseDto.of(null);
    }
}
