package com.example.kare.domain.recomend.controller;


import com.example.kare.common.dto.ResponseDto;
import com.example.kare.domain.recomend.dto.RetrieveRecommendCategoryResDto;
import com.example.kare.domain.recomend.service.RecommendService;
import com.example.kare.domain.today.dto.TodaySearchReqDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/routine-recommendation")
public class RecommendController {
    private final RecommendService recommendService;

    @PostMapping("/routine-category/select")
    public ResponseDto<Map<String, Set<RetrieveRecommendCategoryResDto>>> retrieveRecommendRoutineCategories(){
        Set<RetrieveRecommendCategoryResDto> recommendRoutineCategories = recommendService.findRecommendRoutineCategories();
        Map<String, Set<RetrieveRecommendCategoryResDto>> result = new HashMap<>();
        result.put("recommendRoutineCategoryList", recommendRoutineCategories);

        return ResponseDto.of(result);
    }
}
