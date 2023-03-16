package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
import com.example.kare.entity.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
public class RoutineGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "routine_group_id")
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;
    @OneToMany(mappedBy = "group")
    private List<LinkRoutineGroup> linkRoutineGroups = new ArrayList<>();



    // ******** 생성 함수 ********
    public static RoutineGroup createRoutineGroup(Member member, String name){
        RoutineGroup routineGroup = new RoutineGroup();
        routineGroup.setMember(member);
        routineGroup.setName(name);
        return routineGroup;
    }

}
