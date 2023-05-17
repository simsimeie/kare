package com.example.kare.domain.recomand.service;

import com.example.kare.repository.ReciRoutnCatgMpRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecommandService {
    private final ReciRoutnCatgMpRepo reciRoutnCatgMpRepo;


}
