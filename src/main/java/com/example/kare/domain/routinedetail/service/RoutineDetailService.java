package com.example.kare.domain.routinedetail.service;

import com.example.kare.entity.routine.Routine;
import com.example.kare.repository.RoutineRepoistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineDetailService {
    private final RoutineRepoistory routineRepoistory;
    @Transactional
    public void deleteRoutine(Long routineId, String memberId){
        Optional<Routine> routine = routineRepoistory.findRoutineByIdAndMemberId(routineId, memberId);
        if(routine.isPresent()){
            routineRepoistory.delete(routine.get());
        }
    }
}
