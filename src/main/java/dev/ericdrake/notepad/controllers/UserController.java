package dev.ericdrake.notepad.controllers;

import dev.ericdrake.notepad.dtos.UserDto;
import dev.ericdrake.notepad.exceptions.UserAlreadyExistsException;
import dev.ericdrake.notepad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public List<String> addUser(@RequestBody UserDto userDto){
        String passHash = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(passHash);
        List<String> response = new ArrayList<>();
        try {
            response = userService.addUser(userDto);
        } catch(UserAlreadyExistsException e){
            response.add(e.getMessage());
        }
        return response;
    }

    @PostMapping("/login")
    public List<String> loginUser(@RequestBody UserDto userDto){
        return userService.loginUser(userDto);
    }
}
