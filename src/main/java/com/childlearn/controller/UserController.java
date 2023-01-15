package com.childlearn.controller;

import com.childlearn.dto.CredentialDto;
import com.childlearn.dto.UserDto;
import com.childlearn.entity.User;
import com.childlearn.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("loginForm", new CredentialDto());
        return "login";
    }

    @PostMapping("/authenticate")
    public String authenticate(CredentialDto credentialDto, Model model) throws UserPrincipalNotFoundException {
        Optional<User> user = userService.validateCredential(credentialDto);
        if (user.isPresent()) {
            return "redirect:/";
        } else {
            model.addAttribute("loginForm", credentialDto);
            return "login";
        }
    }

    @GetMapping("/user/create")
    public String getViewCreateUser(UserDto userDto, Model model) {
        model.addAttribute("userForm", new UserDto());
        return "create-user";
    }

    @PostMapping("/user/create")
    public String createUser(UserDto userDto, Model model) {
        log.info("USER DTO: " + userDto.toString());
        User user = new User();

        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setDob(userDto.getDob());

        User createdUser = userService.createUser(user);

        log.info("USER CREATED: " + createdUser.toString());

        return "redirect:/";
    }

}
