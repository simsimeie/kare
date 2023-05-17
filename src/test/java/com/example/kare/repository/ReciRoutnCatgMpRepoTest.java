package com.example.kare.repository;

import com.example.kare.entity.routine.ReciRoutnCatgMp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ReciRoutnCatgMpRepoTest {
    @Autowired
    private ReciRoutnCatgMpRepo reciRoutnCatgMpRepo;

    @Test
    public void temp(){
        List<ReciRoutnCatgMp> allReciRoutnCatg = reciRoutnCatgMpRepo.findAllReciRoutnCatg();
        assertEquals(6, allReciRoutnCatg.size());
    }

}