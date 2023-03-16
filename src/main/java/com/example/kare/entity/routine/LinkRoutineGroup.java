package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
public class LinkRoutineGroup extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name="link_routine_group_id")
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "linkRoutineGroup")
    private Routine routine;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="routine_group_id")
    private RoutineGroup group;
    private Integer displayOrder;


    // ******** 생성 함수 ********
    public static LinkRoutineGroup createLinkRoutineGroup(Routine routine, RoutineGroup group){
        LinkRoutineGroup linkRoutineGroup = new LinkRoutineGroup();
        linkRoutineGroup.setRoutine(routine);
        linkRoutineGroup.setGroup(group);
        linkRoutineGroup.setDisplayOrder(0);

        return linkRoutineGroup;
    }


}
