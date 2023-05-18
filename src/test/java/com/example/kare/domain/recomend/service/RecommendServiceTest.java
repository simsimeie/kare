package com.example.kare.domain.recomend.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RecommendServiceTest {
    @Autowired
    private RecommendService recommendService;
    @Test
    public void test01(){
        recommendService.findRecommendRoutineCategories();
    }

}