package com.example.kare.domain.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CommonAchievementReqDto {
    private String memberId;
    private Integer routineSequence;
    private LocalDate requestDate;

}
