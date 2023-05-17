package com.example.kare.domain.routinegroup.service;


import com.example.kare.common.constant.ErrorCode;
import com.example.kare.common.exception.KBException;
import com.example.kare.domain.routinegroup.dto.*;
import com.example.kare.domain.today.service.CommonService;
import com.example.kare.domain.today.service.MemberService;
import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.MmrRoutnGrpMgt;
import com.example.kare.entity.routine.id.MmrRoutnGrpMgtId;
import com.example.kare.repository.MmrRoutnGrpMgtRepo;
import com.example.kare.repository.MmrRoutnMgtRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RoutineGroupService {
    private final MemberService memberService;
    private final CommonService commonService;
    private final MmrRoutnGrpMgtRepo mmrRoutnGrpMgtRepo;
    private final MmrRoutnMgtRepo mmrRoutnMgtRepo;


    @Transactional
    public MmrRoutnGrpMgtId inputRoutineGroup(String mmrId, String routnGrpNm) {
        Member member = memberService.findMember(mmrId);
        Integer routineGroupSequence = mmrRoutnGrpMgtRepo.findMaxRoutnGrpSeq(member);
        Integer sortOrder = commonService.findMinSoOrd(member);

        MmrRoutnGrpMgt routineGroup = MmrRoutnGrpMgt.createRoutineGroup(
                member,
                routnGrpNm,
                routineGroupSequence,
                sortOrder
        );

        mmrRoutnGrpMgtRepo.save(routineGroup);

        return routineGroup.getId();
    }

    @Transactional
    public CommonRoutineGroupResDto inputRoutineGroup(CreateRoutineGroupReqDto reqDto) {
        MmrRoutnGrpMgt mmrRoutnGrpMgt = createRoutineGroup(reqDto);
        mmrRoutnGrpMgtRepo.save(mmrRoutnGrpMgt);

        return CommonRoutineGroupResDto.of(
                mmrRoutnGrpMgt.getId().getRoutnGrpSeq(),
                mmrRoutnGrpMgt.getId().getMember(),
                mmrRoutnGrpMgt.getRoutnGrpNm()
        );
    }

    private MmrRoutnGrpMgt createRoutineGroup(CreateRoutineGroupReqDto reqDto) {
        Member member = memberService.findMember(reqDto.getMemberId());
        Integer routineGroupSequence = mmrRoutnGrpMgtRepo.findMaxRoutnGrpSeq(member);
        Integer sortOrder = commonService.findMinSoOrd(member);

        return MmrRoutnGrpMgt.createRoutineGroup(
                member,
                reqDto.getRoutineGroupName(),
                routineGroupSequence,
                sortOrder
        );
    }

    public MmrRoutnGrpMgt findRoutineGroup(Integer routnGrpSeq, String memberId) {
        Optional<MmrRoutnGrpMgt> result = mmrRoutnGrpMgtRepo.findById(new MmrRoutnGrpMgtId(routnGrpSeq, memberId));
        if (result.isEmpty()) {
            throw new KBException("존재하지 않는 루틴 그룹입니다.", ErrorCode.BAD_REQUEST);
        }
        return result.get();
    }

    public RetrieveRoutineGroupResDto findRoutineGroupByMember(RetrieveRoutineGroupReqDto reqDto){
        Member member = memberService.findMember(reqDto.getMemberId());
        List<MmrRoutnGrpMgt> routineGroupList = mmrRoutnGrpMgtRepo.findByMember(member);

        RetrieveRoutineGroupResDto resDto = new RetrieveRoutineGroupResDto();

        routineGroupList.forEach(routineGroup ->
                resDto.getDetailResDtoList()
                        .add(CommonRoutineGroupResDto.of(
                                routineGroup.getRoutnGrpSeq(),
                                routineGroup.getMember().getId(),
                                routineGroup.getRoutnGrpNm()
                        )));

        return resDto;
    }

    @Transactional
    public CommonRoutineGroupResDto modifyRoutineGroup(UpdateRoutineGroupReqDto reqDto){
        MmrRoutnGrpMgt routineGroup = findRoutineGroup(reqDto.getRoutineGroupSequence(), reqDto.getMemberId());

        routineGroup.changeRoutineGroupName(reqDto.getRoutineGroupName());

        return CommonRoutineGroupResDto.of(
                routineGroup.getId().getRoutnGrpSeq(),
                routineGroup.getId().getMember(),
                routineGroup.getRoutnGrpNm()
        );
    }


    @Transactional
    public void removeRoutineGroup(DeleteRoutineGroupReqDto reqDto){
        MmrRoutnGrpMgt routineGroup = findRoutineGroup(reqDto.getRoutineGroupSequence(), reqDto.getMemberId());

        mmrRoutnMgtRepo.bulkUpdateRoutnGrpSeqToNull(reqDto.getRoutineGroupSequence(),reqDto.getMemberId());
        mmrRoutnGrpMgtRepo.delete(routineGroup);
    }


}
