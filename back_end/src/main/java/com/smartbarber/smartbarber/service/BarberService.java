package com.smartbarber.smartbarber.service;

import com.smartbarber.smartbarber.repository.BarberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarberService {

    private final BarberRepository barberRepository;


}