package com.example.kare.domain.today.service;


import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.today.dto.RoutineGroupReqDto;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.MmrRoutnGrpMgt;
import com.example.kare.entity.routine.id.MmrRoutnGrpMgtId;
import com.example.kare.repository.MmrRoutnGrpMgtRepo;
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
    private final MmrRoutnGrpMgtRepo routineGroupRepo;


    @Transactional
    public MmrRoutnGrpMgtId inputRoutineGroup(
            String mmrId,
            String routnGrpNm) {
        Member member = memberService.findMember(mmrId);
        Integer routineGroupSequence = routineGroupRepo.findMaxRoutnGrpSeq(member);
        Integer sortOrder = commonService.findMinSoOrd(member);

        MmrRoutnGrpMgt mmrRoutnGrpMgt = MmrRoutnGrpMgt.createRoutineGroup(
                member,
                routnGrpNm,
                routineGroupSequence,
                sortOrder
        );

        routineGroupRepo.save(mmrRoutnGrpMgt);

        return mmrRoutnGrpMgt.getId();
    }

    @Transactional
    public MmrRoutnGrpMgtId inputRoutineGroup(RoutineGroupReqDto routineGroupRequestDto) {
        MmrRoutnGrpMgt mmrRoutnGrpMgt = createRoutineGroup(routineGroupRequestDto);
        routineGroupRepo.save(mmrRoutnGrpMgt);

        return mmrRoutnGrpMgt.getId();
    }

    private MmrRoutnGrpMgt createRoutineGroup(RoutineGroupReqDto routineGroupRequestDto) {
        Member member = memberService.findMember(routineGroupRequestDto.getMemberId());
        Integer routineGroupSequence = routineGroupRepo.findMaxRoutnGrpSeq(member);
        Integer sortOrder = commonService.findMinSoOrd(member);

        return routineGroupRequestDto.toEntity(
                member,
                routineGroupSequence,
                sortOrder
        );
    }

    public MmrRoutnGrpMgt findRoutineGroup(Integer routnGrpSeq, String memberId) {
        Optional<MmrRoutnGrpMgt> routineGroup = routineGroupRepo.findById(new MmrRoutnGrpMgtId(routnGrpSeq, memberId));
        if (routineGroup.isEmpty()) {
            throw new KBException("존재하지 않는 루틴 그룹입니다.", ErrorCode.BAD_REQUEST);
        }
        return routineGroup.get();
    }

}
