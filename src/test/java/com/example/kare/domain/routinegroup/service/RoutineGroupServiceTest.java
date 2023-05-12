package com.example.kare.domain.routinegroup.service;

import com.example.kare.domain.routinegroup.dto.RetrieveRoutineGroupReqDto;
import com.example.kare.domain.routinegroup.dto.RetrieveRoutineGroupResDto;
import com.example.kare.domain.today.service.MemberService;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.member.MemberTest;
import com.example.kare.entity.routine.MmrRoutnGrpMgt;
import com.example.kare.repository.MmrRoutnGrpMgtRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class RoutineGroupServiceTest {
    private Member testMember1;
    private List<MmrRoutnGrpMgt> routnGrpMgtList;
    @InjectMocks
    private RoutineGroupService routineGroupService;
    @Mock
    private MemberService memberService;
    @Mock
    private MmrRoutnGrpMgtRepo routineGroupRepo;

    @BeforeEach
    public void setUp(){
        testMember1 = MemberTest.createMemberForTest("테스트1");

        routnGrpMgtList = new ArrayList<>();

        routnGrpMgtList.add(MmrRoutnGrpMgt.createRoutineGroup(
                testMember1,
                "테스트그룹1",
                1,
                1
        ));

        routnGrpMgtList.add(MmrRoutnGrpMgt.createRoutineGroup(
                testMember1,
                "테스트그룹2",
                2,
                2
        ));

    }

    @Test
    public void findRoutineGroupByMemberTest01(){
        given(memberService.findMember(any())).willReturn(testMember1);
        given(routineGroupRepo.findByMember(testMember1)).willReturn(routnGrpMgtList);

        RetrieveRoutineGroupResDto resDto = routineGroupService.findRoutineGroupByMember(RetrieveRoutineGroupReqDto.of("any"));

        assertEquals(2, resDto.getDetailResDtoList().size());

        assertEquals(testMember1.getId(), resDto.getDetailResDtoList().get(0).getMemberId());
        assertEquals("테스트그룹1", resDto.getDetailResDtoList().get(0).getRoutineGroupName());
        assertEquals(1, resDto.getDetailResDtoList().get(0).getRoutineGroupSequence());

        assertEquals(testMember1.getId(), resDto.getDetailResDtoList().get(1).getMemberId());
        assertEquals("테스트그룹2", resDto.getDetailResDtoList().get(1).getRoutineGroupName());
        assertEquals(2, resDto.getDetailResDtoList().get(1).getRoutineGroupSequence());
    }

}