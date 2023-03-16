package com.example.kare.entity.routine;

import com.example.kare.entity.member.Member;
import com.example.kare.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RoutineTest {

    private MemberRepository memberRepository;
    private RoutineRepoistory routineRepoistory;
    private RoutineGroupRepository routineGroupRepository;

    public RoutineTest(@Autowired MemberRepository memberRepository
            ,@Autowired RoutineRepoistory routineRepoistory
            ,@Autowired RoutineGroupRepository routineGroupRepository
    ){
        this.memberRepository = memberRepository;
        this.routineRepoistory = routineRepoistory;
        this.routineGroupRepository = routineGroupRepository;
    }

    private Member member;
    private Routine routine;
    private RoutineGroup routineGroup01;
    private RoutineGroup routineGroup02;

    @BeforeEach
    public void init(){
        member = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(member);

        routine = RoutineRepositoryTest.createRoutineForTest(member, 1);
        routineRepoistory.save(routine);

        routineGroup01 = RoutineGroupRepositoryTest.createRoutineGroupForTest(member);
        routineGroupRepository.save(routineGroup01);
        routineGroup02 = RoutineGroupRepositoryTest.createRoutineGroupForTest(member);
        routineGroupRepository.save(routineGroup02);
    }

    @Test
    @DisplayName("루틴과 루틴그룹을 잘 연계하는지 테스트")
    public void addRoutineToRoutineGroupTest01(){
        routine.addRoutineToGroup(routineGroup01);
        routineRepoistory.save(routine);

        assertEquals(routine.getLinkRoutineGroup().getGroup().getId() , routineGroup01.getId());
    }

    @Test
    @DisplayName("루틴 그룹 클리어 테스트(orphanRemoval = true 테스트)")
    public void clearRoutineGroupTest01(){
        routine.clearRoutineGroup();
        routineRepoistory.save(routine);

        assertNull(routine.getLinkRoutineGroup());
    }

    @Test
    @DisplayName("루틴의 루틴그룹을 변경했을 때 잘 연계되는지 테스트")
    public void addRoutineToRoutineGroupTest02(){
        routine.addRoutineToGroup(routineGroup01);
        routineRepoistory.save(routine);

        routine.clearRoutineGroup();
        routineRepoistory.save(routine);

        routine.addRoutineToGroup(routineGroup02);
        routineRepoistory.save(routine);

        assertEquals(routine.getLinkRoutineGroup().getGroup().getId() , routineGroup02.getId());
    }

}