package com.example.kare.domain.today.service;

import com.example.kare.domain.today.dto.CreateRoutineRequestDto;
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
    public Long createRoutine(CreateRoutineRequestDto requestDto){
        Routine routine = transformDtoToRoutine(requestDto);
        Routine savedRoutine = routineRepoistory.save(routine);
        return savedRoutine.getId();
    }

    protected Routine transformDtoToRoutine(CreateRoutineRequestDto requestDto){
        Optional<Member> member = memberRepository.findById(requestDto.getMemberId());
        if(member.isEmpty()){
            throw new RuntimeException("존재하지 않는 회원입니다.");
        }

        Integer routineDisplayLeastValue = routineRepoistory.findRoutineDisplayLeastValue(member.get());

        return requestDto.toEntity(member.get(), routineDisplayLeastValue);
    }


}
