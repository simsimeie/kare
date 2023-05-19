package com.example.kare.domain.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class DeleteAchievementReqDto {
    private List<Integer> routineList;
    private String memberId;
    private LocalDate requestDate;
}
