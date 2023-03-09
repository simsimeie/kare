package com.example.kare.domain.today.controller;

import com.example.kare.domain.signup.service.SignupService;
import com.example.kare.domain.today.dto.CreateRoutineRequestDto;
import com.example.kare.domain.today.service.TodayService;
import com.example.kare.domain.today.service.TodayServiceTest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest(TodayController.class)
@SpringBootTest
@AutoConfigureMockMvc
class TodayControllerTest {
    private final MockMvc mvc;
    private final ObjectMapper objectMapper;
    @MockBean
    private TodayService todayService;

    public TodayControllerTest(
            @Autowired MockMvc mvc,
            @Autowired ObjectMapper objectMapper
    ){
        this.mvc = mvc;
        this.objectMapper = objectMapper;
    }

    @Test
    @DisplayName("루틴의 이름이 없을 때 Validation Exception 발생하는지 테스트")
    public void createRoutineValidationTest01() throws Exception {
        CreateRoutineRequestDto routineRequestDtoForTest = TodayServiceTest.createRoutineRequestDtoForTest();
        routineRequestDtoForTest.setName("");

        // when
        mvc.perform(
                        post("/today/routine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(routineRequestDtoForTest)))
                // then
                .andExpect(status().is4xxClientError());
        then(todayService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("루틴의  Validation Exception 발생하는지 테스트")
    public void createRoutineValidationTest02() throws Exception {
        CreateRoutineRequestDto routineRequestDtoForTest = TodayServiceTest.createRoutineRequestDtoForTest();
        routineRequestDtoForTest.getGoal().setGoalValue(99999999);

        // when
        mvc.perform(
                        post("/today/routine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(routineRequestDtoForTest)))
                // then
                .andExpect(status().is4xxClientError());
        then(todayService).shouldHaveNoInteractions();
    }


}