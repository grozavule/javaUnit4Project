package dev.ericdrake.notepad.services;

import dev.ericdrake.notepad.dtos.UserDto;
import dev.ericdrake.notepad.entities.User;
import dev.ericdrake.notepad.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    @Transactional
    public List<String> addUser(UserDto userDto){
        List<String> response = new ArrayList<>();
        User user = new User(userDto);
        userRepository.saveAndFlush(user);
        response.add("User was successfully added");
        return response;
    }

    @Override
    public List<String> loginUser(UserDto userDto){
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
        if(userOptional.isPresent()){
            if(passwordEncoder.matches(userDto.getPassword(), userOptional.get().getPassword())){
                response.add("The username/password entered was accepted");
                response.add(String.valueOf(userOptional.get().getId()));
            } else {
                response.add("The username/password entered is invalid.");
            }
        } else {
            response.add("The username/password entered is invalid.");
        }
        return response;
    }
}
