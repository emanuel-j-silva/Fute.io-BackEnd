package com.io.fute.controller;

import com.io.fute.dto.auth.LoginRequest;
import com.io.fute.dto.auth.RegisterRequest;
import com.io.fute.dto.auth.TokenDTO;
import com.io.fute.entity.AppUser;
import com.io.fute.security.TokenService;
import com.io.fute.service.RegisterUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final RegisterUserService registerUser;

    @Autowired
    public AuthenticationController(TokenService tokenService, AuthenticationManager authenticationManager, RegisterUserService registerUser) {
        this.tokenService = tokenService;
        this.authenticationManager = authenticationManager;
        this.registerUser = registerUser;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest request){
        try{
            var token = new UsernamePasswordAuthenticationToken(request.email(), request.password());
            var authentication = authenticationManager.authenticate(token);

            AppUser user = (AppUser) authentication.getPrincipal();
            var tokenJWT = tokenService.generateToken(user);
            return ResponseEntity.status(HttpStatus.OK).body(new TokenDTO(tokenJWT));
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody @Valid RegisterRequest request){
        AppUser savedUser = registerUser.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}
