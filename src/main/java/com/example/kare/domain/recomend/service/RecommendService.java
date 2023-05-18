package com.example.kare.domain.recomend.service;

import com.example.kare.domain.recomend.dto.RecommendRoutineResDto;
import com.example.kare.domain.recomend.dto.RetrieveRecommendCategoryResDto;
import com.example.kare.entity.routine.ReciRoutnCatgMgt;
import com.example.kare.entity.routine.ReciRoutnCatgMp;
import com.example.kare.entity.routine.ReciRoutnMgt;
import com.example.kare.repository.ReciRoutnCatgMpRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RecommendService {
    private final ReciRoutnCatgMpRepo reciRoutnCatgMpRepo;

    public Set<RetrieveRecommendCategoryResDto> findRecommendRoutineCategories(){
        List<ReciRoutnCatgMp> categoryMapping = reciRoutnCatgMpRepo.findAllReciRoutnCatg();

        Map<Integer, List<RecommendRoutineResDto>> routinesMapByCategory = new HashMap<>();
        Set<RetrieveRecommendCategoryResDto> categroySet = new HashSet<>();

        categoryMapping.forEach(
                map -> {
                    ReciRoutnCatgMgt category = map.getReciRoutnCatgMgt();
                    ReciRoutnMgt routine = map.getReciRoutnMgt();

                    RecommendRoutineResDto routineDto = new RecommendRoutineResDto(routine, map.getSoOrd());
                    List<RecommendRoutineResDto> routineDtoList =
                            Optional.ofNullable(routinesMapByCategory.get(category.getReciRoutnCatgSeq())).orElseGet(ArrayList::new);
                    routineDtoList.add(routineDto);
                    routinesMapByCategory.put(category.getReciRoutnCatgSeq(), routineDtoList);

                    categroySet.add(new RetrieveRecommendCategoryResDto(category));
                }
        );

        for(RetrieveRecommendCategoryResDto retrieveRecommendCategoryResDto : categroySet) {
            retrieveRecommendCategoryResDto.setRecommendRotuineList(routinesMapByCategory.get(retrieveRecommendCategoryResDto.getRecommendRoutineCategorySeq()));
        }

        return categroySet;
    }



}
