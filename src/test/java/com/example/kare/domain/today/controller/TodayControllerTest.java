package com.example.kare.domain.today.controller;

import com.example.kare.domain.today.dto.LinkRoutineGroupRequestDto;
import com.example.kare.domain.today.dto.RoutineGroupRequestDto;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.domain.today.service.TodayService;
import com.example.kare.domain.today.service.TodayServiceMockTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 슬라이스 테스트
 */
//@WebMvcTest(TodayController.class)
/**
 * 통합 테스트
 */
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
    @DisplayName("루틴이 정상적으로 저장되는지 테스트")
    public void createRoutineTest01() throws Exception {
        RoutineRequestDto routineRequestDtoForTest =
                TodayServiceMockTest.createRoutineRequestDtoForTest();
        given(todayService.createRoutine(any())).willReturn(1L);


        mvc.perform(
                        post("/today/routine")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(routineRequestDtoForTest)))
                // then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ok").value(true))
                .andExpect(jsonPath("$.code").value("200"))
                .andExpect(jsonPath("$.message").value("성공"))
                .andExpect(jsonPath("$.body").value(1L));
    }

    @Test
    @DisplayName("루틴의 이름이 없을 때 Validation Exception 발생하는지 테스트")
    public void createRoutineValidationTest01() throws Exception {
        RoutineRequestDto routineRequestDtoForTest
                = TodayServiceMockTest.createRoutineRequestDtoForTest();
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
    @DisplayName("루틴의 목표량을 7자리 초과 정수로 설정했을 때, Validation Exception 발생하는지 테스트")
    public void createRoutineValidationTest02() throws Exception {
        RoutineRequestDto routineRequestDtoForTest
                = TodayServiceMockTest.createRoutineRequestDtoForTest();
        routineRequestDtoForTest.getGoal().setGoalValue(10000000);

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
    @DisplayName("루틴의 주별 회수를 1미만으로 설정했을 때, Validation Exception 발생하는지 테스트")
    public void createRoutineValidationTest03() throws Exception {
        RoutineRequestDto routineRequestDtoForTest
                = TodayServiceMockTest.createRoutineRequestDtoForTest();
        routineRequestDtoForTest.getCycle().setCount(0);
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
    @DisplayName("루틴의 주별 회수를 7초과로 했을 때, Exception 발생하는지 테스트")
    public void createRoutineValidationTest04() throws Exception {
        RoutineRequestDto routineRequestDtoForTest
                = TodayServiceMockTest.createRoutineRequestDtoForTest();
        routineRequestDtoForTest.getCycle().setCount(8);
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
    @DisplayName("그룹의 이름을 빈 칸으로 넣고 루틴 그룹을 생성할 때, Exception 발생하는지 테스트")
    public void createRoutineGroupValidationTest01() throws Exception{
        RoutineGroupRequestDto routineGroupRequestDtoForTest = RoutineGroupRequestDto.of("test_member", "");
        mvc.perform(
                    post("/today/routine-group")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(routineGroupRequestDtoForTest)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.code").value(400));
    }


    @Test
    @DisplayName("루틴의 id에 0이하의 수가 들어오고 루틴과 그룹을 연결할 때, Validation Exception 발생하는지 테스트")
    public void linkRoutineGroupValidationTest01() throws Exception{
        LinkRoutineGroupRequestDto linkRoutineGroupRequestDto = LinkRoutineGroupRequestDto.of(0L, null);
        mvc.perform(
                        post("/today/link/routine-group")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(linkRoutineGroupRequestDto)))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.ok").value(false))
                .andExpect(jsonPath("$.code").value(400));
    }


}