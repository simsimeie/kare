package com.example.kare.repository;

import com.example.kare.domain.today.dto.CycleDto;
import com.example.kare.domain.today.dto.GoalDto;
import com.example.kare.entity.routine.RoutineGroup;
import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import com.example.kare.entity.routine.Cycle;
import com.example.kare.entity.routine.Goal;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
/**
 * 실제 DB로 테스트할 때
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
@Rollback(false)
public class RoutineRepositoryTest {
    private final RoutineRepository routineRepository;
    private MemberRepository memberRepository;
    private final TestEntityManager em;
    @Autowired
    private RoutineGroupRepository routineGroupRepository;
    @MockBean
    private RoutineRepositoryMyBatisImpl routineRepositoryMyBatis;

    public RoutineRepositoryTest(
            @Autowired RoutineRepository routineRepository
            ,@Autowired MemberRepository memberRepository
            ,@Autowired TestEntityManager entityManager
    ){
        this.routineRepository = routineRepository;
        this.memberRepository = memberRepository;
        this.em = entityManager;
    }

    @Test
    @DisplayName("루틴이 정상적으로 저장되는지 테스트")
    public void routineSaveTest01(){
        Member member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        Integer displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);

        Routine routine = createRoutineForTest(member, displayLeastValue);

        Routine savedRoutine = routineRepository.save(routine);
        assertEquals(savedRoutine.getId(), routine.getId());
    }

    @Test
    @DisplayName("루틴과 루틴그룹이 정상적으로 저장되는지 테스트")
    public void routineSaveTest02(){
        //given
        Member member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        Integer displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);

        Routine routine = createRoutineForTest(member, displayLeastValue);

        routineRepository.save(routine);

        RoutineGroup routineGroupForTest = RoutineGroupRepositoryTest.createRoutineGroupForTest(member);

        routineGroupRepository.save(routineGroupForTest);

        routine.addRoutineToGroup(routineGroupForTest);

        //when
        Routine savedRoutine = routineRepository.save(routine);

        //then
        assertEquals(savedRoutine.getId(), routine.getId());
        assertEquals(savedRoutine.getLinkRoutineGroup().getGroup().getId(), routineGroupForTest.getId());
        assertEquals(savedRoutine.getLinkRoutineGroup().getGroup().getName(), routineGroupForTest.getName());



    }

    @Test
    @DisplayName("회원이 등록한 루틴 display order 값 중 가장 작은 값을 정상적으로 가져오는지 테스트")
    public void findRoutineMinDisplayOrderTest01(){
        //given
        Member member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        Integer displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);

        Routine routine = createRoutineForTest(member, displayLeastValue);
        routineRepository.save(routine);

        //when
        Integer leastValue = routineRepository.findRoutineDisplayLeastValue(member);
        //then
        assertEquals(0, leastValue);
    }

    @Test
    @DisplayName("회원이 등록한 루틴이 없을 때 Display least value 값으로 null을 가져오는지 테스트")
    public void findRoutineMinDisplayOrderTest02(){
        //given
        Member member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        //when
        Integer leastValue = routineRepository.findRoutineDisplayLeastValue(member);
        //then
        assertEquals(null, leastValue);
    }

    @Test
    @DisplayName("회원이 루틴을 처음 등록할 때 Display least value 값으로 1 가져오는지 테스트")
    public void findRoutineMinDisplayOrderTest03(){
        //given
        Member member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        Integer displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);

        Routine routine = createRoutineForTest(member, displayLeastValue);
        routineRepository.save(routine);

        //when
        Integer leastValue = routineRepository.findRoutineDisplayLeastValue(member);
        //then
        assertEquals(0, leastValue);
    }

    @Test
    @DisplayName("루틴의 display order 최소 값 가져오는 거래를 여러번 호출 테스트")
    public void severalTimeRequestMinDisplayValueTest(){
        //given
        Member member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);


        Integer displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);
        Routine routine = createRoutineForTest(member, displayLeastValue);
        routineRepository.save(routine);
        displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);
        routine = createRoutineForTest(member, displayLeastValue);
        routineRepository.save(routine);
        displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);
        routine = createRoutineForTest(member, displayLeastValue);
        routineRepository.save(routine);
        displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);
        routine = createRoutineForTest(member, displayLeastValue);
        routineRepository.save(routine);
        displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);
        routine = createRoutineForTest(member, displayLeastValue);
        routineRepository.save(routine);
        displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);
        routine = createRoutineForTest(member, displayLeastValue);
        routineRepository.save(routine);
        displayLeastValue = routineRepository.findRoutineDisplayLeastValue(member);
        routine = createRoutineForTest(member, displayLeastValue);
        routineRepository.save(routine);
        assertEquals(-5, displayLeastValue);

    }

    @Test
    @DisplayName("루틴 삭제 테스트")
    public void deleteRoutineTest01(){
        //given
        Member member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);
        Integer displayOrder = routineRepository.findRoutineDisplayLeastValue(member);
        Routine routine = RoutineRepositoryTest.createRoutineForTest(member,displayOrder);
        routineRepository.save(routine);

        //when
        routineRepository.delete(routine);
        //then
        assertEquals(Optional.empty(), routineRepository.findById(routine.getId()));
    }


    public static Routine createRoutineForTest(Member member, Integer displayLeastValue) {
        Cycle cycle = Cycle.createCycle(CycleDto.of(CycleType.DAY, true, true, true, true, true, true, false, null));
        Goal goal = Goal.createGoal(GoalDto.of(1, GoalUnit.TIMES));

        return Routine.createRoutine(
                "미라클모닝"
                , member
                , false
                , cycle, goal
                , LocalTime.of(5, 0)
                , LocalDate.now()
                , null
                , displayLeastValue);

    }
}