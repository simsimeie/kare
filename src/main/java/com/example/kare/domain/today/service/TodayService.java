package com.example.kare.domain.today.service;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.domain.today.dto.LinkRoutineGroupRequestDto;
import com.example.kare.domain.today.dto.RoutineGroupRequestDto;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import com.example.kare.entity.routine.RoutineGroup;
import com.example.kare.repository.MemberRepository;
import com.example.kare.repository.RoutineGroupRepository;
import com.example.kare.repository.RoutineRepoistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayService {
    private final MemberRepository memberRepository;
    private final RoutineRepoistory routineRepoistory;
    private final RoutineGroupRepository routineGroupRepository;

    @Transactional
    public Long createRoutine(RoutineRequestDto routineRequestDto){
        Routine routine = transformRoutineRequestDtoToRoutine(routineRequestDto);
        routineRepoistory.save(routine);
        return routine.getId();
    }

    protected Routine transformRoutineRequestDtoToRoutine(RoutineRequestDto routineRequestDto){
        Member member = getMember(routineRequestDto.getMemberId());
        Integer routineDisplayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member);
        return routineRequestDto.toEntity(member, routineDisplayLeastValue);
    }

    private Member getMember(String memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if(member.isEmpty()){
            throw new KBException("존재하지 않는 회원입니다.", ErrorCode.BAD_REQUEST);
        }
        return member.get();
    }

    @Transactional
    public Long createRoutineGroup(RoutineGroupRequestDto routineGroupRequestDto){
        RoutineGroup routineGroup = transformRoutineGroupRequestDtoToRoutineGroup(routineGroupRequestDto);
        routineGroupRepository.save(routineGroup);
        return routineGroup.getId();
    }

    protected RoutineGroup transformRoutineGroupRequestDtoToRoutineGroup(RoutineGroupRequestDto routineGroupRequestDto){
        Member member = getMember(routineGroupRequestDto.getMemberId());
        return routineGroupRequestDto.toEntity(
                member
                , routineGroupRequestDto.getName()
        );
    }

    @Transactional
    public Long linkRoutineAndRoutineGroup(LinkRoutineGroupRequestDto linkRoutineGroupRequestDto){
        Routine routine = getRoutine(linkRoutineGroupRequestDto.getRoutineId());

        if(null != routine.getLinkRoutineGroup()){
            routine.clearRoutineGroup();
            routineRepoistory.save(routine);
        }

        if(null != linkRoutineGroupRequestDto.getRoutineGroupId()){
            RoutineGroup routineGroup = getRoutineGroup(linkRoutineGroupRequestDto.getRoutineGroupId());
            routine.addRoutineToGroup(routineGroup);
            routineRepoistory.save(routine);
        }

        return routine.getId();
    }

    private Routine getRoutine(Long routineId) {
        Optional<Routine> routine = routineRepoistory.findById(routineId);
        if(routine.isEmpty()){
            throw new KBException("존재하지 않는 루틴입니다.", ErrorCode.BAD_REQUEST);
        }
        return routine.get();
    }

    private RoutineGroup getRoutineGroup(Long routineGroupId){
        Optional<RoutineGroup> routineGroup = routineGroupRepository.findById(routineGroupId);
        if(routineGroup.isEmpty()){
            throw new KBException("존재하지 않는 루틴 그룹입니다.", ErrorCode.BAD_REQUEST);
        }
        return routineGroup.get();
    }




}
