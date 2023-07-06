package com.opencode.practice.controllers;

import com.opencode.practice.exceptions.AccessExeption;
import com.opencode.practice.models.Role;
import com.opencode.practice.models.UserStatus;
import com.opencode.practice.models.User;
import com.opencode.practice.repos.ReposUser;
import com.opencode.practice.security.jwts.JwtTokenProwider.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private ReposUser reposUser;
    private JwtTokenProvider jwtTokenProvider;


    public AuthenticationController(AuthenticationManager authenticationManager, ReposUser reposUser, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.reposUser = reposUser;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> create(@RequestBody User user) {
        user.setPassword(String.valueOf(new BCryptPasswordEncoder(8).encode(user.getPassword())));
        user.setRole(Role.USER);
        user.setUserStatus(UserStatus.ACTIVE);
        reposUser.save(user);
        return new ResponseEntity<>("Создан новый аккаунт", HttpStatus.OK);
    }



    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationDTO request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            User user = reposUser.findByEmail(request.getEmail()).orElseThrow(() -> new UsernameNotFoundException("User doesn't exists"));
            String token = jwtTokenProvider.createToken(user.getEmail(), user.getRole().name(), user.getUsername(), String.valueOf(user.getId()));
            Map<Object, Object> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            throw new AccessExeption("Invalid email/password combination");
        }
    }


    @PostMapping("/signout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
        return new ResponseEntity<>("Пользователь вышел из аккаунта", HttpStatus.OK);
    }
}