package com.example.kare.repository;

import com.example.kare.domain.routine.dto.CycleDto;
import com.example.kare.domain.routine.dto.CycleDtoTest;
import com.example.kare.domain.routine.dto.GoalDto;
import com.example.kare.domain.routine.dto.GoalDtoTest;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.member.MemberTest;
import com.example.kare.entity.routine.MmrRoutnMgt;
import com.example.kare.entity.routine.constant.GoalUnitTypeCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
class MmrRoutnMgtRepoTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private MmrRoutnMgtRepo mmrRoutnMgtRepo;
    private Member testMember;
    private MmrRoutnMgt testRoutine;

    @BeforeEach
    public void setup(){
        testMember = MemberTest.createMemberForTest("테스트멤버");
        GoalDto goalDto = GoalDtoTest.createGoalTypeThree(GoalUnitTypeCode.TIMES, 10);
        CycleDto cycleDto = CycleDtoTest.createCycleDtoTimesType(7);

        testRoutine = MmrRoutnMgt.createRoutine(
                "테스트루틴01",
                1,
                testMember,
                "Y",
                LocalTime.of(5, 0),
                cycleDto.toEntity(),
                goalDto.toEntity(),
                LocalDate.now(),
                null,
                1
        );
    }

    @Test
    @DisplayName("")
    public void test01(){

    }

}