package com.example.kare.domain.routine.service;


import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.entity.routine.value.Cycle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoutineDetailServiceTest {
    @InjectMocks
    private RoutineDetailService routineDetailService;
    @Mock
    private MmrRoutnDtlMgt routineDetail;
    @Mock
    private Cycle cycle;

    @BeforeEach
    public void setup() {
        given(routineDetail.getCycle()).willReturn(cycle);
        given(cycle.getMonYn()).willReturn("Y");
        given(cycle.getTueYn()).willReturn("N");
        given(cycle.getWedYn()).willReturn("Y");
        given(cycle.getThuYn()).willReturn("N");
        given(cycle.getFriYn()).willReturn("Y");
        given(cycle.getSatYn()).willReturn("N");
        given(cycle.getSunYn()).willReturn("Y");

    }

    @Test
    @DisplayName("Cycle에 등록된 날짜에 유효한지 테스트")
    public void isTargetForDayTest01() {
        //given & when
        boolean flag = routineDetailService.isTargetForDay(routineDetail, LocalDate.of(2023, Month.MAY, 1));
        //then
        assertEquals(true, flag);

        //given & when
        flag = routineDetailService.isTargetForDay(routineDetail, LocalDate.of(2023, Month.MAY, 2));
        //then
        assertEquals(false, flag);

        //given & when
        flag = routineDetailService.isTargetForDay(routineDetail, LocalDate.of(2023, Month.MAY, 3));
        //then
        assertEquals(true, flag);

        //given & when
        flag = routineDetailService.isTargetForDay(routineDetail, LocalDate.of(2023, Month.MAY, 4));
        //then
        assertEquals(false, flag);

        //given & when
        flag = routineDetailService.isTargetForDay(routineDetail, LocalDate.of(2023, Month.MAY, 5));
        //then
        assertEquals(true, flag);

        //given & when
        flag = routineDetailService.isTargetForDay(routineDetail, LocalDate.of(2023, Month.MAY, 6));
        //then
        assertEquals(false, flag);

        //given & when
        flag = routineDetailService.isTargetForDay(routineDetail, LocalDate.of(2023, Month.MAY, 7));
        //then
        assertEquals(true, flag);
    }
}