package com.example.kare.repository;

import com.example.kare.entity.routine.constant.CycleType;
import com.example.kare.entity.routine.constant.GoalUnit;
import com.example.kare.entity.member.constant.Sex;
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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
/**
 * 실제 DB로 테스트할 때
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
@Rollback(false)
class RoutineRepoistoryTest {
    private final RoutineRepoistory routineRepoistory;
    private final TestEntityManager em;
    @Autowired
    private MemberRepository memberRepository;

    public RoutineRepoistoryTest(
            @Autowired RoutineRepoistory routineRepoistory
            ,@Autowired TestEntityManager entityManager
    ){
        this.routineRepoistory = routineRepoistory;
        this.em = entityManager;
    }

    @Test
    @DisplayName("루틴이 정상적으로 저장되는지 테스트")
    public void routineSaveTest01(){
        Member member = Member.createMember("ABCDE","KYH", LocalDate.of(2021, Month.SEPTEMBER, 25), "01012341234", Sex.FEMALE);
        memberRepository.save(member);

        Cycle cycle = new Cycle(CycleType.DAY, true, true, true, true, true, true, false);
        Goal goal = new Goal(1, GoalUnit.TIMES);
        Integer displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);

        Routine routine = Routine.createRoutine("미라클모닝", member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, displayLeastValue);

        Routine savedRoutine = routineRepoistory.save(routine);
        assertEquals(savedRoutine.getId(), routine.getId());
    }

    @Test
    @DisplayName("회원이 등록한 루틴 display order 값 중 가장 작은 값을 정상적으로 가져오는지 테스트")
    public void findRoutineMinDisplayOrderTest01(){
        //given
        Member member = Member.createMember("ABCDE","KYH", LocalDate.of(2021, Month.SEPTEMBER, 25), "01012341234", Sex.FEMALE);
        memberRepository.save(member);

        Cycle cycle = new Cycle(CycleType.DAY, true, true, true, true, true, true, false);
        Goal goal = new Goal(1, GoalUnit.TIMES);
        Integer displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);

        Routine routine = Routine.createRoutine("미라클모닝",member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, 1  );
        routineRepoistory.save(routine);

        //when
        Integer leastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        //then
        assertEquals(0, leastValue);
    }

    @Test
    @DisplayName("회원이 등록한 루틴이 없을 때 Display least value 값으로 null을 가져오는지 테스트")
    public void findRoutineMinDisplayOrderTest02(){
        //given
        Member member = Member.createMember("ABCDE","KYH", LocalDate.of(2021, Month.SEPTEMBER, 25), "01012341234", Sex.FEMALE);
        memberRepository.save(member);

        //when
        Integer leastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        //then
        assertEquals(null, leastValue);
    }

    @Test
    @DisplayName("회원이 루틴을 처음 등록할 때 Display least value 값으로 1 가져오는지 테스트")
    public void findRoutineMinDisplayOrderTest03(){
        //given
        Member member = Member.createMember("ABCDE","KYH", LocalDate.of(2021, Month.SEPTEMBER, 25), "01012341234", Sex.FEMALE);
        memberRepository.save(member);

        Cycle cycle = new Cycle(CycleType.DAY, true, true, true, true, true, true, false);
        Goal goal = new Goal(1, GoalUnit.TIMES);
        Integer displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);

        Routine routine = Routine.createRoutine("미라클모닝",member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, displayLeastValue );
        routineRepoistory.save(routine);

        //when
        Integer leastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        //then
        assertEquals(0, leastValue);
    }

    @Test
    @DisplayName("루틴의 display order 최소 값 가져오는 거래를 여러번 호출 테스트")
    public void severalTimeRequestMinDisplayValueTest(){
        //given
        Member member = Member.createMember("ABCDE","KYH", LocalDate.of(2021, Month.SEPTEMBER, 25), "01012341234", Sex.FEMALE);
        memberRepository.save(member);

        Cycle cycle = new Cycle(CycleType.DAY, true, true, true, true, true, true, false);
        Goal goal = new Goal(1, GoalUnit.TIMES);

        Integer displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        Routine routine = Routine.createRoutine("미라클모닝",member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, displayLeastValue );
        routineRepoistory.save(routine);
        displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        routine = Routine.createRoutine("미라클모닝",member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, displayLeastValue );
        routineRepoistory.save(routine);
        displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        routine = Routine.createRoutine("미라클모닝",member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, displayLeastValue );
        routineRepoistory.save(routine);
        displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        routine = Routine.createRoutine("미라클모닝",member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, displayLeastValue );
        routineRepoistory.save(routine);
        displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        routine = Routine.createRoutine("미라클모닝",member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, displayLeastValue );
        routineRepoistory.save(routine);
        assertEquals(-3, displayLeastValue);
        displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        routine = Routine.createRoutine("미라클모닝",member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, displayLeastValue );
        routineRepoistory.save(routine);
        displayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        routine = Routine.createRoutine("미라클모닝",member,false, cycle, goal, LocalTime.of(5,0), LocalDate.now(), null, displayLeastValue );
        routineRepoistory.save(routine);
        assertEquals(-5, displayLeastValue);

    }

}