package com.example.kare.domain.today.service;


import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.RoutineGroupRequestDto;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.RoutineGroup;
import com.example.kare.entity.routine.RoutineGroupId;
import com.example.kare.repository.RoutineGroupRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoutineGroupService {
    private final MemberService memberService;
    private final CommonService commonService;
    private final RoutineGroupRepo routineGroupRepo;


    @Transactional
    public RoutineGroupId inputRoutineGroup(
            String mmrId,
            String routnGrpNm) {
        Member member = memberService.findMember(mmrId);
        Integer routineGroupSequence = routineGroupRepo.findMaxRoutnGrpSeq(member);
        Integer sortOrder = commonService.findMinSoOrd(member);

        RoutineGroup routineGroup = RoutineGroup.createRoutineGroup(
                member,
                routnGrpNm,
                routineGroupSequence,
                sortOrder
        );

        routineGroupRepo.save(routineGroup);

        return routineGroup.getId();
    }

    @Transactional
    public RoutineGroupId inputRoutineGroup(RoutineGroupRequestDto routineGroupRequestDto) {
        RoutineGroup routineGroup = createRoutineGroup(routineGroupRequestDto);
        routineGroupRepo.save(routineGroup);

        return routineGroup.getId();
    }

    private RoutineGroup createRoutineGroup(RoutineGroupRequestDto routineGroupRequestDto) {
        Member member = memberService.findMember(routineGroupRequestDto.getMemberId());
        Integer routineGroupSequence = routineGroupRepo.findMaxRoutnGrpSeq(member);
        Integer sortOrder = commonService.findMinSoOrd(member);

        return routineGroupRequestDto.toEntity(
                member,
                routineGroupSequence,
                sortOrder
        );
    }

    public RoutineGroup findRoutineGroup(Integer routnGrpSeq, String memberId) {
        Optional<RoutineGroup> routineGroup = routineGroupRepo.findById(new RoutineGroupId(routnGrpSeq, memberId));
        if (routineGroup.isEmpty()) {
            throw new KBException("존재하지 않는 루틴 그룹입니다.", ErrorCode.BAD_REQUEST);
        }
        return routineGroup.get();
    }

}
