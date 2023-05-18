package com.example.kare.domain.recomend.dto;

import com.example.kare.entity.routine.ReciRoutnCatgMgt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class RetrieveRecommendCategoryResDto {
    private Integer recommendRoutineCategorySeq;
    private String categoryName;
    private String categoryImageFileName;
    private Integer sortOrder;
    private List<RecommendRoutineResDto> recommendRotuineList = new ArrayList<>();

    public RetrieveRecommendCategoryResDto(ReciRoutnCatgMgt category) {
        this.recommendRoutineCategorySeq = category.getReciRoutnCatgSeq();
        this.categoryName = category.getCatgDsc();
        this.categoryImageFileName = category.getCatgImgFlNm();
        this.sortOrder = category.getSoOdr();
    }
}
