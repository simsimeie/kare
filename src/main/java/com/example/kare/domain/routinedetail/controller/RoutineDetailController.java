package com.example.kare.domain.routinedetail.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.routinedetail.service.RoutineDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoutineDetailController {
    private final RoutineDetailService routineDetailService;

    @DeleteMapping("/routine-detail/routine")
    public ResponseDto<Void> deleteRoutine(Integer routnSeq, String memberId ){
        routineDetailService.deleteRoutine(routnSeq, memberId);
        return ResponseDto.of(null);
    }
}
