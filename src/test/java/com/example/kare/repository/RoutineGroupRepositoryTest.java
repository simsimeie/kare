package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.RoutineGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
/**
 * 실제 DB로 테스트할 때
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
@Rollback(false)
public class RoutineGroupRepositoryTest {
    private final RoutineGroupRepository routineGroupRepository;
    private final TestEntityManager em;
    private final MemberRepository memberRepository;
    @MockBean
    private RoutineRepositoryMyBatisImpl routineRepositoryMyBatis;

    public RoutineGroupRepositoryTest(
            @Autowired RoutineGroupRepository routineGroupRepository
            ,@Autowired MemberRepository memberRepository
            ,@Autowired TestEntityManager entityManager
    ){
        this.routineGroupRepository = routineGroupRepository;
        this.memberRepository = memberRepository;
        this.em = entityManager;
    }

    @Test
    @DisplayName("routine group 정상저장되는지 테스트")
    public void routineGroupSaveTest01(){
        Member normalMemberForTest = MemberRepositoryTest.createMemberForTest();
        memberRepository.save(normalMemberForTest);

        RoutineGroup normalRoutineGroupForTest = createRoutineGroupForTest(normalMemberForTest);
        RoutineGroup saved = routineGroupRepository.save(normalRoutineGroupForTest);
        assertEquals(saved.getId(), normalRoutineGroupForTest.getId());
    }



    public static RoutineGroup createRoutineGroupForTest(Member member){
        return RoutineGroup.createRoutineGroup(member,"테스트그룹", 1);
    }




}