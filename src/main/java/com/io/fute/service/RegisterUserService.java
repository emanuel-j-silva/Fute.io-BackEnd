package com.io.fute.service;

import com.io.fute.dto.auth.RegisterRequest;
import com.io.fute.entity.AppUser;
import com.io.fute.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterUserService{
    private final AppUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public RegisterUserService(AppUserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser register(RegisterRequest request){
        String errors = "";
        if (userRepository.existsByUsername(request.username())){
            errors += "Um usuário com esse nome já existe\n";
        }
        if (userRepository.existsByEmail(request.email())){
            errors += "Um usuário com esse email já existe";
        }

        if (!errors.isEmpty()){
            throw new IllegalArgumentException(errors);
        }

        AppUser user = new AppUser(request.name(), request.username(),
                request.email(), passwordEncoder.encode(request.password()));

        return userRepository.save(user);
    }
}
