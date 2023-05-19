package com.example.kare.domain.achievement.dto;

import com.example.kare.domain.routine.dto.GoalDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CreateAchievementDetailReqDto {
    private Integer routineSequence;
    private GoalDto goal;

}
