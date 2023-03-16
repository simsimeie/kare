package com.example.kare.domain.today.service;

import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.RoutineRequestDto;
import com.example.kare.domain.today.dto.LinkRoutineGroupRequestDto;
import com.example.kare.domain.today.dto.RoutineGroupRequestDto;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.Routine;
import com.example.kare.entity.routine.RoutineGroup;
import com.example.kare.entity.routine.RoutineHistory;
import com.example.kare.repository.MemberRepository;
import com.example.kare.repository.RoutineGroupRepository;
import com.example.kare.repository.RoutineHistoryRepository;
import com.example.kare.repository.RoutineRepoistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodayService {
    private final MemberRepository memberRepository;
    private final RoutineRepoistory routineRepoistory;
    private final RoutineGroupRepository routineGroupRepository;
    private final RoutineHistoryRepository routineHistoryRepository;

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
    public Long modifyRoutine(RoutineRequestDto routineRequestDto){
        Routine routine = getRoutine(routineRequestDto.getRoutineId());
        Routine modifiedRoutine = routine.modifyRoutine(routineRequestDto);
        routineRepoistory.save(modifiedRoutine);

        RoutineHistory routineHistory = getLastRoutineHistory(routine);

        if(routineHistory.isShouldUpdateRoutineHistory(modifiedRoutine)){
            if(Period.between(LocalDate.now(), routineRequestDto.getStartDate() ).isNegative()){
                throw new KBException("루틴의 시작 일자는 과거로 설정할 수 없습니다.", ErrorCode.BAD_REQUEST);
            }
            // 현재 <= 변경할 start date <= Last History의 start date : history update
            if(! Period.between(routineRequestDto.getStartDate(), routineHistory.getStartDate()).isNegative()){
                routineHistory.modifyRoutineHistory(modifiedRoutine);
                routineHistoryRepository.save(routineHistory);
            }
            // 현재 <= Last History의 start date < 변경할 start date : history insert
            else{
                // 오늘 생성해서 오늘 시작날짜를 변경한 경우 endDate에 오늘 날짜 들어가야 함
                LocalDate endDate = routineHistory.getStartDate() == LocalDate.now() ? LocalDate.now() : LocalDate.now().minusDays(1);
                routineHistory.changeRoutineHistoryEndDate(endDate);
                routineHistoryRepository.save(routineHistory);
                RoutineHistory newRoutineHistory = RoutineHistory.createRoutineHistory(modifiedRoutine, LocalDate.now());
                routineHistoryRepository.save(newRoutineHistory);
            }

        }

        return modifiedRoutine.getId();
    }

    private RoutineHistory getLastRoutineHistory(Routine routine) {
        RoutineHistory last = routineHistoryRepository.findLastHistoryByRoutineAndEndDate(
                routine
                , LocalDate.of(9999, 12, 31)
        );
        return last;
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
