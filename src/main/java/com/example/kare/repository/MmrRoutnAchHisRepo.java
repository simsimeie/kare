package com.example.kare.repository;

import com.example.kare.entity.routine.MmrRoutnAhvHis;
import com.example.kare.entity.routine.id.MmrRoutnAhvHisId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MmrRoutnAchHisRepo extends JpaRepository<MmrRoutnAhvHis, MmrRoutnAhvHisId>, MmrRoutnAchHisRepoCustom {
    Optional<MmrRoutnAhvHis> findById(MmrRoutnAhvHisId id);
}
