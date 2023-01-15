package com.childlearn.restcontroller;

import com.childlearn.dto.CredentialDto;
import com.childlearn.entity.User;
import com.childlearn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserRestController {

    @Autowired
    private UserService service;

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        List<User> users = service.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User newUser = service.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody CredentialDto credentialDto) throws UserPrincipalNotFoundException {
        log.info(credentialDto.toString());
        Optional<User> loginUser = service.validateCredential(credentialDto);
        if (!loginUser.isEmpty()) {
            return ResponseEntity.ok("Login berhasil!");
        } else {
            return ResponseEntity.badRequest().body("Username atau password tidak valid.");
        }

    }

}
