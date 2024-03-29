package com.example.kare.repository;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.member.MemberTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
class MmrRoutnMgtRepoTest {
    @Autowired
    private TestEntityManager em;
    @Autowired
    private MmrRoutnMgtRepo mmrRoutnMgtRepo;
    private Member testMember;

    @BeforeEach
    public void init(){
        testMember = MemberTest.createMemberForTest();
    }

    @Test
    @DisplayName("")
    public void test01(){

    }

}