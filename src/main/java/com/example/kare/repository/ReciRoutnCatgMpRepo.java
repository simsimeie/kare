package com.example.kare.repository;

import com.example.kare.entity.routine.ReciRoutnCatgMp;
import com.example.kare.entity.routine.id.ReciRoutnCatgMpId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReciRoutnCatgMpRepo extends JpaRepository<ReciRoutnCatgMp, ReciRoutnCatgMpId> {
    @Query("select rcm from ReciRoutnCatgMp rcm join fetch rcm.reciRoutnCatgMgt join fetch rcm.reciRoutnMgt")
    List<ReciRoutnCatgMp> findAllReciRoutnCatg();
}
