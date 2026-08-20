package com.smartbarber.smartbarber.service;

import com.smartbarber.smartbarber.entity.User;
import com.smartbarber.smartbarber.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;


}