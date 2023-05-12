package com.example.kare.domain.routine.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.routine.dto.CreateRoutineAchieveReqDto;
import com.example.kare.domain.routine.service.RoutineDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routine-achievement")
public class RoutineDetailController {
    private final RoutineDetailService routineDetailService;

    //TODO : Validation 체크 추가
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
