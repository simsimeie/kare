package com.example.kare.domain.routinedetail.service;

import com.example.kare.entity.routine.Routine;
import com.example.kare.entity.routine.RoutineId;
import com.example.kare.repository.RoutineRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineDetailService {
    private final RoutineRepo routineRepo;
    @Transactional
    public void deleteRoutine(Integer routnSeq, String memberId){
        Optional<Routine> routine = routineRepo.findById(new RoutineId(routnSeq, memberId));
        if(routine.isPresent()){
            routineRepo.delete(routine.get());
        }
    }
}
