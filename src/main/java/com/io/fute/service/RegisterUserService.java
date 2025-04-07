package com.io.fute.service;

import com.io.fute.dto.LoginRequest;
import com.io.fute.dto.RegisterRequest;
import com.io.fute.entity.AppUser;
import com.io.fute.repository.AppUserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService{
    private AppUserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegisterUserService(AppUserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser register(RegisterRequest request){
        if (userRepository.existsByUsername(request.username())){
            throw new IllegalArgumentException("Um usuário com esse nome já existe");
        }

        AppUser user = new AppUser(request.name(), request.username(),
                request.email(), passwordEncoder.encode(request.password()));

        return userRepository.save(user);
    }
}
