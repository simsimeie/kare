package com.example.kare.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MmrRoutnAchHisRepoTest {

    @Autowired
    private TestEntityManager em;
    @Autowired
    private MmrRoutnAchHisRepo mmrRoutnAchHisRepo;
    @Autowired
    private MmrRoutnMgtRepo mmrRoutnMgtRepo;

    @BeforeEach
    public void setup(){

    }
    @Test
    public void bulkDeleteRoutineAchievementTest01(){
    }
}