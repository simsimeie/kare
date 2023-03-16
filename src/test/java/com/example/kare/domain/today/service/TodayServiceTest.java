package com.example.kare.domain.today.service;

import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import com.example.kare.entity.routine.RoutineHistory;
import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import com.example.kare.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class TodayServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoutineRepoistory routineRepoistory;
    @Autowired
    private TodayService todayService;
    private Member member;
    private Routine routine;
    private RoutineRequestDto mockRoutineRequestDto;
    @Autowired
    private RoutineHistoryRepository routineHistoryRepository;

    @BeforeEach
    public void init(){
        member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        CycleDto cycleDto = CycleDto.of(CycleType.DAY, true, true, true, true, true, true, false, null);
        GoalDto goalDto = GoalDto.of(1, GoalUnit.TIMES);

        routine = RoutineRepositoryTest.createRoutineForTest(member, 1);
        routineRepoistory.save(routine);
        mockRoutineRequestDto = Mockito.mock(RoutineRequestDto.class);
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
        given(mockRoutineRequestDto.getStartDate()).willReturn(LocalDate.now().minusDays(1));
        assertThrows(KBException.class, ()->todayService.modifyRoutine(mockRoutineRequestDto));
    }

}