package com.example.kare.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

import static org.junit.jupiter.api.Assertions.*;

class MmrRoutnDtlMgtRepoTest {

    @Test
    @DisplayName("Period 학습 테스트")
    public void getNumOfTargetDatesTest01(){
        LocalDate start = LocalDate.of(2023, Month.MAY, 1);
        LocalDate end = LocalDate.of(2023, Month.MAY, 1);
        assertEquals(0, Period.between(start, end).getDays());

        start = LocalDate.of(2023, Month.MAY, 1);
        end = LocalDate.of(2023, Month.MAY, 31);
        assertEquals(30, Period.between(start, end).getDays());

        start = LocalDate.of(2023, Month.MAY, 20);
        end = LocalDate.of(2023, Month.MAY, 10);
        assertEquals(-10, Period.between(start, end).getDays());

        start = LocalDate.of(2023, Month.MAY, 11);
        end = LocalDate.of(2023, Month.MAY, 10);
        assertEquals(-1, Period.between(start, end).getDays());

        start = LocalDate.of(2023, Month.MAY, 29);
        end = LocalDate.of(2023, Month.JUNE, 4);
        assertEquals(6, Period.between(start, end).getDays());
    }

}