package com.example.kare.repository;

import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.domain.today.dto.RoutineResponseDto;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.*;
import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
public class RoutineRepositoryMyBatisImplTest {
    private final EntityManager entityManager;
    private final MemberRepository memberRepository;
    private final RoutineRepository routineRepository;
    private final RoutineGroupRepository routineGroupRepository;
    public RoutineRepositoryMyBatisImplTest(
            @Autowired EntityManager entityManager
            ,@Autowired MemberRepository memberRepository
            ,@Autowired RoutineRepository routineRepository
            ,@Autowired RoutineGroupRepository routineGroupRepository
    ){
        this.entityManager = entityManager;
        this.memberRepository = memberRepository;
        this.routineRepository = routineRepository;
        this.routineGroupRepository = routineGroupRepository;
    }
    @Test
    @DisplayName("Cycle Type이 주별 횟수로 지정된 미래의 루틴 정보 제대로 select 하는지 테스트")
    public void findFutureRoutineTest01(){
        //given
        Member member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        RoutineGroup routineGroup = RoutineGroupRepositoryTest.createRoutineGroupForTest(member);
        routineGroupRepository.save(routineGroup);

        Routine routine = RoutineRepositoryTest.createRoutineForTest(member, 1);
        routine.addRoutineToGroup(routineGroup);
        routineRepository.save(routine);

        entityManager.flush();
        entityManager.clear();

        //when
        List<RoutineResponseDto> todayRoutines = routineRepository.findFutureRoutines(member.getId(), LocalDate.of(2023, Month.MARCH, 17));
        //then
        assertEquals(1, todayRoutines.size());
        RoutineResponseDto routineResponseDto = todayRoutines.get(0);
        assertEquals(routineResponseDto.getRoutineName(), routine.getName());
        assertEquals(routineResponseDto.getRoutineId(), routine.getId());
        assertEquals(routineResponseDto.getCycleCount(), routine.getCycle().getCycleCount());
        assertEquals(routineResponseDto.getCycleType(), routine.getCycle().getCycleType());
        assertEquals(routineResponseDto.getGoalUnit(), routine.getGoal().getGoalUnit());
        assertEquals(routineResponseDto.getGoalValue(), routine.getGoal().getGoalValue());
        assertEquals(routineResponseDto.getRoutineGroupId(), routineGroup.getId());
        assertEquals(routineResponseDto.getRoutineGroupName(), routineGroup.getName());
    }


    @Test
    @DisplayName("Cycle Type이 금요일로 지정된 미래의 루틴 정보 제대로 select 하는지 테스트")
    public void findFutureRoutineTest02(){
        //given
        Member member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        RoutineGroup routineGroup = RoutineGroupRepositoryTest.createRoutineGroupForTest(member);
        routineGroupRepository.save(routineGroup);

        Cycle cycle1 = CycleDto.of(CycleType.DAY, false, false, false, false, true, false, false, null).toEntity();
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

        routine1.addRoutineToGroup(routineGroup);
        routineRepository.save(routine1);
        routineRepository.save(routine2);

        entityManager.flush();
        entityManager.clear();

        //when
        List<RoutineResponseDto> todayRoutines = routineRepository.findFutureRoutines(member.getId(), LocalDate.of(2023, Month.MARCH, 17));
        //then
        assertEquals(2, todayRoutines.size());

    }
}