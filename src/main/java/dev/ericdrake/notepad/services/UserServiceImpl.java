package dev.ericdrake.notepad.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ericdrake.notepad.dtos.UserDto;
import dev.ericdrake.notepad.entities.User;
import dev.ericdrake.notepad.exceptions.UserAlreadyExistsException;
import dev.ericdrake.notepad.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
    public List<String> addUser(UserDto userDto) throws UserAlreadyExistsException {
        //check if the username entered into the registration form is already in use
        if(userRepository.findByUsername(userDto.getUsername()).isPresent()){
            throw new UserAlreadyExistsException(UserAlreadyExistsException.ERROR);
        }

        List<String> response = new ArrayList<>();
        try {
            User user = new User(userDto);
            userRepository.saveAndFlush(user);

            ObjectMapper mapper = new ObjectMapper();
            //I don't like that I can't set the User.password field as @Transient or set it to null before passing it
            //back to the controller. Is there another way to avoid sending the encrypted hash back to the front end?
            String userJson = mapper.writeValueAsString(user);
            response.add("SUCCESS");
            response.add(userJson);
        } catch(Exception e){
            response.add("ERROR");
            response.add(e.getMessage());
            return response;
        }
        return response;
    }

    @Override
    public List<String> loginUser(UserDto userDto){
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findByUsername(userDto.getUsername());
        if(userOptional.isPresent()){
            if(passwordEncoder.matches(userDto.getPassword(), userOptional.get().getPassword())){
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    String userJson = mapper.writeValueAsString(userOptional.get());
                    response.add("SUCCESS");
                    response.add(userJson);
                    //response.add(String.valueOf(userOptional.get().getId()));
                } catch(Exception e){
                    response.add("ERROR");
                    response.add(e.getMessage());
                }
            } else {
                response.add("ERROR");
                response.add("The username/password entered is invalid.");
            }
        } else {
            response.add("ERROR");
            response.add("The username/password entered is invalid.");
        }
        return response;
    }
}
