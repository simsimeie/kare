package com.example.kare.domain.achievement.controller;

import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.achievement.dto.CreateAchievementReqDto;
import com.example.kare.domain.achievement.dto.DeleteAchievementReqDto;
import com.example.kare.domain.achievement.service.RoutineAchievementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routine-achievement")
public class RoutineAchievementController {
    private final RoutineAchievementService routineDetailService;

    //TODO : Validation 체크 추가 - 테스트의 어려움 때문에 @Valid 제거
    @PostMapping("/record/save")
    public ResponseDto<Void> saveRoutineAchievement(@RequestBody CreateAchievementReqDto reqDto){
        routineDetailService.saveRoutineAchievement(reqDto);
        return ResponseDto.of(null);
    }

    @PostMapping("/record/delete")
    public ResponseDto<Void> removeRoutineAchievement(@RequestBody DeleteAchievementReqDto reqDto){
        routineDetailService.removeRoutineAchievement(reqDto);
        return ResponseDto.of(null);
    }

//    @DeleteMapping("/routine-detail/routine")
//    public ResponseDto<Void> deleteRoutine(Integer routnSeq, String memberId ){
//        routineDetailService.deleteRoutine(routnSeq, memberId);
//        return ResponseDto.of(null);
//    }
}
