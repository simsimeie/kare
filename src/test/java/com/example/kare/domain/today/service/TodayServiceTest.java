package com.example.kare.domain.today.service;

import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.*;
import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import com.example.kare.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class TodayServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoutineRepository routineRepository;
    @Autowired
    private TodayService todayService;
    @Autowired
    private RoutineHistoryRepository routineHistoryRepository;
    private Member member;
    private Routine routine;
    private RoutineRequestDto mockRoutineRequestDto;
    private RoutineGroup routineGroup;
    @Autowired
    private RoutineGroupRepository routineGroupRepository;


    @BeforeEach
    public void init(){
        member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        routineGroup = RoutineGroupRepositoryTest.createRoutineGroupForTest(member);
        routineGroupRepository.save(routineGroup);

        routine = RoutineRepositoryTest.createRoutineForTest(member, 1);
        routine.addRoutineToGroup(routineGroup);
        routineRepository.save(routine);

        mockRoutineRequestDto = Mockito.mock(RoutineRequestDto.class);
        CycleDto cycleDto = CycleDto.of(CycleType.DAY, true, true, true, true, true, true, false, null);
        GoalDto goalDto = GoalDto.of(1, GoalUnit.TIMES);
        given(mockRoutineRequestDto.getRoutineId()).willReturn(routine.getId());
        given(mockRoutineRequestDto.getName()).willReturn(routine.getName());
        given(mockRoutineRequestDto.getGoal()).willReturn(goalDto);
        given(mockRoutineRequestDto.getCycle()).willReturn(cycleDto);
        given(mockRoutineRequestDto.getAlarmTime()).willReturn(routine.getAlarmTime());
        given(mockRoutineRequestDto.getStartDate()).willReturn(routine.getStartDate());
    }

    @Test
    @DisplayName("루틴 이름만 바뀌어서 들어올 때(주기, 목표, 시작시간 외의 값), RoutineHistory 변경되지 않는지 테스트")
    public void modifyRoutineTest01(){
        //given
        given(mockRoutineRequestDto.getName()).willReturn("미라클저녁");
        RoutineHistory history = routineHistoryRepository.findLastHistoryByRoutineAndEndDate(routine, LocalDate.of(9999, 12, 31));
        LocalDateTime lastModifiedDate = history.getLastModifiedDate();

        //when
        todayService.modifyRoutine(mockRoutineRequestDto);
        RoutineHistory history2 = routineHistoryRepository.findLastHistoryByRoutineAndEndDate(routine, LocalDate.of(9999, 12, 31));

        //then
        assertEquals(history2.getLastModifiedDate(), lastModifiedDate);
    }

    @Test
    @DisplayName("루틴 주기 변경되었을 때, RoutineHistory 변경되는지 테스트")
    public void modifyRoutineTest02(){
        //given
        CycleDto cycleDto = CycleDto.of(CycleType.DAY, true, true, true, false, true, true, false, null);

        given(mockRoutineRequestDto.getCycle()).willReturn(cycleDto);
        RoutineHistory history = routineHistoryRepository.findLastHistoryByRoutineAndEndDate(routine, LocalDate.of(9999, 12, 31));
        LocalDateTime lastModifiedDate = history.getLastModifiedDate();

        //when
        todayService.modifyRoutine(mockRoutineRequestDto);
        RoutineHistory history2 = routineHistoryRepository.findLastHistoryByRoutineAndEndDate(routine, LocalDate.of(9999, 12, 31));

        //then
        assertNotEquals(history2.getLastModifiedDate(), lastModifiedDate);
        assertEquals(false, history2.getCycle().isThu());
    }

    @Test
    @DisplayName("과거날짜로 startDate를 변경했을 때, KB Exception 발생하는지 테스트")
    public void modifyRoutineTest03(){
        //given
        given(mockRoutineRequestDto.getStartDate()).willReturn(LocalDate.now().minusDays(1));
        //then
        assertThrows(KBException.class, ()->todayService.modifyRoutine(mockRoutineRequestDto));
    }

    @Test
    @DisplayName("이미 시작된 루틴의 startDate를 변경했을 때, KB Exception 발생하는지 테스트")
    public void modifyRoutineTest04(){
        //given
        CycleDto cycleDto = CycleDto.of(CycleType.DAY, true, true, true, true, true, true, false, null);
        GoalDto goalDto = GoalDto.of(1, GoalUnit.TIMES);

        Routine proceedingRoutine = Routine.createRoutine(
                "미라클점심"
                , member
                , true
                , cycleDto.toEntity()
                , goalDto.toEntity()
                , LocalTime.of(12, 00, 00)
                , LocalDate.now().minusDays(10)
                , null
                , 1
        );

        routineRepository.save(proceedingRoutine);

        given(mockRoutineRequestDto.getStartDate()).willReturn(LocalDate.now().plusDays(1));
        given(mockRoutineRequestDto.getRoutineId()).willReturn(proceedingRoutine.getId());


        assertThrows(KBException.class, ()->todayService.modifyRoutine(mockRoutineRequestDto));
    }

    @Test
    @DisplayName("루틴 조회 서비스 테스트")
    public void retrieveRoutineTest01(){
        Cycle cycle1 = CycleDto.of(CycleType.DAY, true, true, true, true, true, true, false, null).toEntity();
        Cycle cycle2 = CycleDto.of(CycleType.TIMES, false, false, false, false, false, false, false, 3).toEntity();
        Goal goal = GoalDto.of(1000, GoalUnit.TIMES).toEntity();

        Routine routine1 = Routine.createRoutine(
                "미라클모닝"
                , member
                , true
                , cycle1
                , goal
                , LocalTime.of(5, 0, 0)
                , LocalDate.of(2023, Month.MARCH, 17)
                , null
                , 1
        );


        Routine routine2 = Routine.createRoutine(
                "미라클점심"
                , member
                , true
                , cycle2
                , goal
                , LocalTime.of(12, 0, 0)
                , LocalDate.of(2023, Month.MARCH, 17)
                , null
                , 1
        );
        routine2.addRoutineToGroup(routineGroup);
        routineRepository.save(routine1);
        routineRepository.save(routine2);

        todayService.retrieveRoutine(member.getId(), LocalDate.now().plusDays(1));

    }

}