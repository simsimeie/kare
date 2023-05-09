package com.example.kare.domain.today.dto;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.MmrRoutnGrpMgt;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class RoutineGroupReqDto {
    @NotEmpty
    private String memberId;
    @NotBlank
    private String name;

    public MmrRoutnGrpMgt toEntity(Member member, Integer routnGrpSeq, Integer soOdr) {
        return MmrRoutnGrpMgt.createRoutineGroup(
                member
                , this.name
                , routnGrpSeq
                , soOdr
        );
    }
}
