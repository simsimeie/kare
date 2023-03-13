package com.example.kare.entity.routine;

import com.example.kare.entity.BaseTimeEntity;
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
    @OneToMany(mappedBy = "group", orphanRemoval = true)
    private List<LinkRoutineGroup> linkRoutineGroups = new ArrayList<>();

}
