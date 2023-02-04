package dev.ericdrake.notepad.services;

import dev.ericdrake.notepad.dtos.UserDto;
import dev.ericdrake.notepad.entities.User;
import dev.ericdrake.notepad.exceptions.UserAlreadyExistsException;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserService {
    @Transactional
    List<String> addUser(UserDto userDto) throws UserAlreadyExistsException;

    List<String> loginUser(UserDto userDto);
}
