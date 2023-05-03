package com.example.kare.domain.today.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class TodaySearchRequestDto {
    private String memberId;
    private LocalDate searchDate;
}
