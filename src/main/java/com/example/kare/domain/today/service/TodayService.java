package com.example.kare.domain.today.service;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import com.example.kare.repository.MemberRepository;
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

    @Transactional
    public Long createRoutine(RoutineRequestDto requestDto){
        Routine routine = transformDtoToRoutine(requestDto);
        Routine savedRoutine = routineRepoistory.save(routine);
        return savedRoutine.getId();
    }

    protected Routine transformDtoToRoutine(RoutineRequestDto requestDto){
        Optional<Member> member = memberRepository.findById(requestDto.getMemberId());

        if(member.isEmpty()){
            throw new KBException("존재하지 않는 회원입니다.", ErrorCode.BAD_REQUEST);
        }

        Integer routineDisplayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member.get());

        return requestDto.toEntity(member.get(), routineDisplayLeastValue);
    }


}
