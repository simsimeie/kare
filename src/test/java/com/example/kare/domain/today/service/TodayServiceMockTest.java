package com.example.kare.domain.today.service;

import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.*;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import com.example.kare.entity.routine.RoutineGroup;
import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import com.example.kare.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TodayServiceMockTest {
    @InjectMocks
    private TodayService todayService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RoutineRepository routineRepository;
    @Mock
    private RoutineGroupRepository routineGroupRepository;

    @Test
    @DisplayName("루틴 조회 서비스 테스트")
    public void retrieveRoutineTest01(){
        LocalDate now = LocalDate.now();
        todayService.retrieveRoutine("x", now);

        LocalDate future = LocalDate.now().plusDays(1);
        todayService.retrieveRoutine("x", future);
        then(routineRepository).should(only()).findFutureRoutines(any(),any());

        LocalDate past = LocalDate.now().minusDays(1);
        todayService.retrieveRoutine("x", past);
    }

    @Test
    @DisplayName("존재하지 않는 회원ID로 루틴을 만들 때 Exception 발생 여부 테스트")
    public void notExistsMember_createRoutine_exception_test01(){
        //given
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        //then
        Exception exception = assertThrows(KBException.class, ()->{
            // when
            todayService.createRoutine(Mockito.mock(RoutineRequestDto.class));
        });
    }

    @Test
    @DisplayName("RoutineRequestDto가 Routine entity로 잘 변환되는지 테스트")
    public void transform_RoutineRequestDtoToRoutine_test01(){
        //given
        given(memberRepository.findById(any())).willReturn(Optional.of(MemberRepositoryTest.createMemberForTest()));
        RoutineRequestDto routineRequestDtoForTest = createRoutineRequestDtoForTest();
        given(routineRepository.findRoutineDisplayLeastValue(any())).willReturn(1);

        //when
        Routine routine = todayService.transformRoutineRequestDtoToRoutine(routineRequestDtoForTest);

        //then
        assertEquals("미라클 모닝", routine.getName());
        assertEquals(true, routine.isAlarm());
        assertEquals(CycleType.DAY, routine.getCycle().getCycleType());
        assertEquals(true, routine.getCycle().isMon());
        assertEquals(true, routine.getCycle().isTue());
        assertEquals(false, routine.getCycle().isWed());
        assertEquals(true, routine.getCycle().isThu());
        assertEquals(true, routine.getCycle().isFri());
        assertEquals(false, routine.getCycle().isSat());
        assertEquals(true, routine.getCycle().isSun());
        assertEquals(1, routine.getGoal().getGoalValue());
        assertEquals(GoalUnit.TIMES, routine.getGoal().getGoalUnit());
        assertEquals(LocalTime.of(5,0), routine.getAlarmTime());
        assertEquals(LocalDate.now(), routine.getStartDate());
        assertEquals(null, routine.getEndDate());
        assertEquals( 1, routine.getDisplayOrder());

    }
    @Test
    @DisplayName("존재하지 않는 회원ID로 루틴그룹을 만들 때 Exception 발생 여부 테스트")
    public void notExistsMember_createRoutineGroup_exception_test01(){
        //given
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        //then
        Exception exception = assertThrows(KBException.class, ()->{
            // when
            todayService.createRoutineGroup(Mockito.mock(RoutineGroupRequestDto.class));
        });
    }

    @Test
    @DisplayName("RoutineGroupRequestDto가 루틴그룹 엔티티로 잘 변환되는지 테스트")
    public void transform_RoutineGroupRequestDtoToRoutineGroup_test01(){
        //given
        given(memberRepository.findById(any())).willReturn(Optional.of(MemberRepositoryTest.createMemberForTest()));
        RoutineGroupRequestDto routineGroupRequestDtoForTest = createRoutineGroupRequestDtoForTest();

        //when
        RoutineGroup routineGroup = todayService.transformRoutineGroupRequestDtoToRoutineGroup(routineGroupRequestDtoForTest);

        //then
        assertEquals("테스트 그룹", routineGroup.getName());
        assertEquals("test_member", routineGroup.getMember().getName());
    }

    @Test
    @DisplayName("RoutineGroupRequestDto에 존재하지 않은 routineId가 들어왔을 때, KBException 발생하는지 테스트")
    public void routineIdIsInvalid_calllinkRoutineAndRoutineGroup_KBException_Test01(){
        //given
        given(routineRepository.findById(any())).willReturn(Optional.empty());
        LinkRoutineGroupRequestDto mock = mock(LinkRoutineGroupRequestDto.class);
        given(mock.getRoutineId()).willReturn(1L);
        //then
        Exception exception = assertThrows(KBException.class, ()->{
            //when
            todayService.linkRoutineAndRoutineGroup(mock);
        });

        //then
        then(routineRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("RoutineGroupRequestDto에 존재하지 않은 routineGroupId가 들어왔을 때, KBException 발생하는지 테스트")
    public void routineGroupIdIsInvalid_calllinkRoutineAndRoutineGroup_KBException_Test01(){
        //given
        Member member = MemberRepositoryTest.createMemberForTest();
        given(routineRepository.findById(any())).willReturn(Optional.of(Mockito.mock(Routine.class)));
        given(routineGroupRepository.findById(any())).willReturn(Optional.empty());
        LinkRoutineGroupRequestDto mock = mock(LinkRoutineGroupRequestDto.class);
        given(mock.getRoutineGroupId()).willReturn(1L);
        given(mock.getRoutineId()).willReturn(1L);

        //then
        Exception exception = assertThrows(KBException.class, ()->{
            //when
            todayService.linkRoutineAndRoutineGroup(mock);
        });
    }


    public static RoutineRequestDto createRoutineRequestDtoForTest(){
        CycleDto cycleDto = new CycleDto(CycleType.DAY, true, true, false, true, true, false, true);

        return new RoutineRequestDto(
                "미라클 모닝"
                ,"test_member"
                ,true
                , cycleDto
                , GoalDto.of(1, GoalUnit.TIMES)
                , LocalTime.of(5,0)
                , LocalDate.now()
                ,null
        );
    }

    public static RoutineGroupRequestDto createRoutineGroupRequestDtoForTest(){
        return RoutineGroupRequestDto.of("test_member","테스트 그룹");
    }


}