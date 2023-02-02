package dev.ericdrake.notepad.services;

import dev.ericdrake.notepad.dtos.UserDto;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserService {
    @Transactional
    List<String> addUser(UserDto userDto);

    List<String> loginUser(UserDto userDto);
}
