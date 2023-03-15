package com.example.kare.domain.today.dto;

import com.example.kare.entity.member.Member;
import com.example.kare.entity.routine.RoutineGroup;
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
public class RoutineGroupRequestDto {
    @NotEmpty
    private String memberId;
    @NotBlank
    private String name;

    public RoutineGroup toEntity(Member member, String name){
        return RoutineGroup.createRoutineGroup(
                member
                , name
        );
    }
}
