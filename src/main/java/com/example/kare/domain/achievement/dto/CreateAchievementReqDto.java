package com.example.kare.domain.achievement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class CreateAchievementReqDto {
    private String memberId;
    private @Valid List<CreateAchievementDetailReqDto> routineList;
    @PastOrPresent
    private LocalDate requestDate;

}
