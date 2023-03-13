package com.example.kare.repository;

import com.example.kare.entity.member.constant.Sex;
import com.example.kare.entity.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
/**
 * 실제 DB로 테스트할 때
 */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.yml")
@Rollback(false)
public class MemberRepositoryTest {

    private final MemberRepository memberRepository;
    private final TestEntityManager em;

    public MemberRepositoryTest(
            @Autowired MemberRepository memberRepository,
            @Autowired TestEntityManager entityManager
            ){
        this.memberRepository = memberRepository;
        this.em = entityManager;
    }
    @Test
    @DisplayName("회원 데이터 저장 테스트")
    public void saveMemberTest01(){
        Member member = Member.createMember("ABCDE","KYH", LocalDate.of(2021, Month.SEPTEMBER, 25), "01012341234", Sex.FEMALE);
        Member saved = memberRepository.save(member);
        assertEquals(saved.getId(), member.getId());
    }

    @Test
    @DisplayName("CI 값으로 회원을 조회하는 테스트")
    public void findMemberTest01(){
        Member member = Member.createMember("ABCDE","KYH", LocalDate.of(2021, Month.SEPTEMBER, 25), "01012341234", Sex.FEMALE);
        memberRepository.save(member);
        Member memberByCi = memberRepository.findMemberByCi(member.getCi());
        assertEquals(memberByCi.getId(), member.getId());
    }

    public static Member createNormalMemberForTest(){
        return Member.createMember("ABCDE","KYH", LocalDate.of(2021, Month.SEPTEMBER, 25), "01012341234", Sex.FEMALE);
    }
}