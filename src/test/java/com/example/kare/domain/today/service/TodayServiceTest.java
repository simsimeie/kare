package com.example.kare.domain.today.service;

import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.entity.routine.Routine;
import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import com.example.kare.repository.MemberRepository;
import com.example.kare.repository.MemberRepositoryTest;
import com.example.kare.repository.RoutineRepoistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TodayServiceTest {
    @InjectMocks
    private TodayService todayService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RoutineRepoistory routineRepoistory;

    @Test
    @DisplayName("존재하지 않는 회원ID로 루틴을 만들 때 Exception 발생 여부 테스트")
    public void notExistsMember_createRoutine_exception_test01(){
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        Exception exception = assertThrows(KBException.class, ()->{
            // when
            todayService.createRoutine(new RoutineRequestDto());
        });
    }

    @Test
    @DisplayName("루틴 정상 저장 여부 확인 테스트")
    public void createRoutine_test01(){
        //given
        given(memberRepository.findById(any())).willReturn(Optional.of(MemberRepositoryTest.createNormalMemberForTest()));
        RoutineRequestDto routineRequestDtoForTest = createRoutineRequestDtoForTest();
        given(routineRepoistory.findRoutineDisplayLeastValue(any())).willReturn(1);

        Routine routine = todayService.transformDtoToRoutine(routineRequestDtoForTest);
        routine.setId(1L);
        given(routineRepoistory.save(any())).willReturn(routine);

        //when
        Long routineId = todayService.createRoutine(routineRequestDtoForTest);
        assertEquals(1, routineId);
    }


    public static RoutineRequestDto createRoutineRequestDtoForTest(){
        CycleDto cycleDto = new CycleDto(CycleType.DAY, true, true, false, true, true, false, true);

        return RoutineRequestDto.of(
                "미라클 모닝"
                ,"member1"
                ,true
                , cycleDto
                , GoalDto.of(1, GoalUnit.TIMES)
                , LocalTime.of(5,0)
                , LocalDate.now()
                ,null
        );
    }


}