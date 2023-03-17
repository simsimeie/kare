package com.example.kare.domain.routinedetail.service;

import com.example.kare.repository.RoutineRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;


@ExtendWith(MockitoExtension.class)
public class RoutineDetailServiceMockTest {
    @InjectMocks
    private RoutineDetailService routineDetailService;
    @Mock
    private RoutineRepository routineRepository;

    @Test
    @DisplayName("주어진 memberId, routineId에 해당하는 routine이 없을 때 아무것도 삭제되지 않는지 테스트")
    public void routineDeleteTest01(){
        //given
        given(routineRepository.findRoutineByIdAndMemberId(any(),any())).willReturn(Optional.empty());
        //when
        routineDetailService.deleteRoutine(any(),any());
        //then
        then(routineRepository).should(never()).delete(any());
    }



}