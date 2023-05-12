package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.member.MemberTest;
import com.example.kare.entity.routine.MmrRoutnGrpMgt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
@DataJpaTest
class MmrRoutnGrpMgtRepoTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MmrRoutnGrpMgtRepo mmrRoutnGrpMgtRepo;
    @Autowired
    private TestEntityManager em;
    private Member testMember1;
    private Member testMember2;


    @BeforeEach
    public void setUp(){
        testMember1 = MemberTest.createMemberForTest("테스트1");
        testMember2 = MemberTest.createMemberForTest("테스트2");

        memberRepository.save(testMember1);
        memberRepository.save(testMember2);
    }

    @Test
    @DisplayName("Member Entity 기반으로 MmrRoutnGrpMgt 리스트 가져오는지 테스트")
    public void findMmrRoutnGrpMgtByMemberTest01(){

        MmrRoutnGrpMgt group1 = createRoutineGroup(testMember1, "테스트그룹1", 1, 1);
        MmrRoutnGrpMgt group2 = createRoutineGroup(testMember1, "테스트그룹2", 2, 2);
        MmrRoutnGrpMgt group3 = createRoutineGroup(testMember1, "테스트그룹3", 3, 3);
        MmrRoutnGrpMgt group4 = createRoutineGroup(testMember1, "테스트그룹4", 4, 4);
        MmrRoutnGrpMgt group5 = createRoutineGroup(testMember2, "테스트그룹5", 1, 1);

        mmrRoutnGrpMgtRepo.save(group1);
        mmrRoutnGrpMgtRepo.save(group2);
        mmrRoutnGrpMgtRepo.save(group3);
        mmrRoutnGrpMgtRepo.save(group4);
        mmrRoutnGrpMgtRepo.save(group5);

        List<MmrRoutnGrpMgt> testMember1Group = mmrRoutnGrpMgtRepo.findByMember(testMember1);
        List<MmrRoutnGrpMgt> testMember2Group = mmrRoutnGrpMgtRepo.findByMember(testMember2);

        assertEquals(4, testMember1Group.size());
        assertEquals(1, testMember2Group.size());

    }




    public static MmrRoutnGrpMgt createRoutineGroup(
            Member member,
            String routineGroupName,
            Integer routineGroupSequence,
            Integer sortOrder
    ){

        return MmrRoutnGrpMgt.createRoutineGroup(
                member,
                routineGroupName,
                routineGroupSequence,
                sortOrder
        );
    }

}