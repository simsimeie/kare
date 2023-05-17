package com.example.kare.repository;

import com.example.kare.domain.routine.dto.CycleDto;
import com.example.kare.domain.routine.dto.CycleDtoTest;
import com.example.kare.domain.routine.dto.GoalDto;
import com.example.kare.domain.routine.dto.GoalDtoTest;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.member.MemberTest;
import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.MmrRoutnDtlMgt;
import com.example.kare.entity.routine.MmrRoutnMgt;
import com.example.kare.entity.routine.constant.AchieveStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MmrRoutnAchHisRepoTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private MmrRoutnAchHisRepo mmrRoutnAchHisRepo;
    @Autowired
    private MmrRoutnMgtRepo mmrRoutnMgtRepo;
    @Autowired
    private MemberRepository memberRepo;

    private Member testMember;
    private MmrRoutnMgt testRoutine01;
    private MmrRoutnMgt testRoutine02;


    @BeforeEach
    public void setup(){
        testMember  = MemberTest.createMemberForTest("테스트유저");
        memberRepo.save(testMember);

        CycleDto testCycle = CycleDtoTest.createCycleDtoTimesType(7);
        GoalDto testGoal = GoalDtoTest.createGoalTypeOne();

        testRoutine01 = MmrRoutnMgt.createRoutine(
                "테스트루틴01",
                1,
                testMember,
                "Y",
                LocalTime.of(5, 0),
                testCycle.toEntity(),
                testGoal.toEntity(),
                LocalDate.of(2023, Month.MAY, 1),
                null,
                1
        );

        mmrRoutnMgtRepo.save(testRoutine01);
        MmrRoutnDtlMgt testRoutineDetail01 = testRoutine01.getRoutineDetailList().get(0);

        MmrRoutnAhvHis testRoutineAchievement01 = MmrRoutnAhvHis.createRoutineAchievement(
                LocalDate.of(2023, Month.MAY, 1),
                testRoutineDetail01.getRoutnChDt(),
                testRoutineDetail01.getRoutnSeq(),
                testRoutineDetail01.getMmrId(),
                testRoutineDetail01.getGoal(),
                AchieveStatus.Y
        );

        MmrRoutnAhvHis testRoutineAchievement02 = MmrRoutnAhvHis.createRoutineAchievement(
                LocalDate.of(2023, Month.MAY, 2),
                testRoutineDetail01.getRoutnChDt(),
                testRoutineDetail01.getRoutnSeq(),
                testRoutineDetail01.getMmrId(),
                testRoutineDetail01.getGoal(),
                AchieveStatus.Y
        );

        MmrRoutnAhvHis testRoutineAchievement03 = MmrRoutnAhvHis.createRoutineAchievement(
                LocalDate.of(2023, Month.MAY, 3),
                testRoutineDetail01.getRoutnChDt(),
                testRoutineDetail01.getRoutnSeq(),
                testRoutineDetail01.getMmrId(),
                testRoutineDetail01.getGoal(),
                AchieveStatus.Y
        );

        mmrRoutnAchHisRepo.save(testRoutineAchievement01);
        mmrRoutnAchHisRepo.save(testRoutineAchievement02);
        mmrRoutnAchHisRepo.save(testRoutineAchievement03);


        testRoutine02 = MmrRoutnMgt.createRoutine(
                "테스트루틴02",
                2,
                testMember,
                "Y",
                LocalTime.of(5, 0),
                testCycle.toEntity(),
                testGoal.toEntity(),
                LocalDate.of(2023, Month.MAY, 1),
                null,
                1
        );

        mmrRoutnMgtRepo.save(testRoutine02);
        MmrRoutnDtlMgt testRoutineDetail02 = testRoutine02.getRoutineDetailList().get(0);

        MmrRoutnAhvHis testRoutineAchievement04 = MmrRoutnAhvHis.createRoutineAchievement(
                LocalDate.of(2023, Month.MAY, 3),
                testRoutineDetail02.getRoutnChDt(),
                testRoutineDetail02.getRoutnSeq(),
                testRoutineDetail02.getMmrId(),
                testRoutineDetail02.getGoal(),
                AchieveStatus.Y
        );
        mmrRoutnAchHisRepo.save(testRoutineAchievement04);
    }
    @Test
    @DisplayName("단건 루틴 기록 삭제 테스트")
    public void bulkDeleteRoutineAchievementTest01(){
        //given
        List<Integer> routineSequenceList = List.of(testRoutine01.getRoutnSeq());
        //when
        Integer deleted = mmrRoutnAchHisRepo.bulkDeleteRoutnAhv(routineSequenceList, testMember.getId(), LocalDate.of(2023, Month.MAY, 1));
        //then
        assertEquals(1, deleted);
    }

    @Test
    @DisplayName("다건 루틴 기록 삭제 테스트")
    public void bulkDeleteRoutineAchievementTest02(){
        //given
        List<Integer> routineSequenceList = List.of(testRoutine01.getRoutnSeq(), testRoutine02.getRoutnSeq());
        //when
        Integer deleted = mmrRoutnAchHisRepo.bulkDeleteRoutnAhv(routineSequenceList, testMember.getId(), LocalDate.of(2023, Month.MAY, 3));
        //then
        assertEquals(2, deleted);
    }
}