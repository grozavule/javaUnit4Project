package dev.ericdrake.notepad.services;

import dev.ericdrake.notepad.dtos.NoteDto;
import dev.ericdrake.notepad.dtos.UserDto;
import dev.ericdrake.notepad.entities.Note;
import dev.ericdrake.notepad.entities.User;
import dev.ericdrake.notepad.repositories.NoteRepository;
import dev.ericdrake.notepad.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public List<String> addNote(NoteDto noteDto, Long userId){
        List<String> response = new ArrayList<>();
        Optional<User> userOptional = userRepository.findById(userId);
        Note note = new Note(noteDto);
        if(userOptional.isPresent()){
            note.setUser(userOptional.get());
        }
        noteRepository.saveAndFlush(note);
        response.add("Your note was successfully added");
        return response;
    }

    @Override
    @Transactional
    public List<String> deleteNote(Long noteId){
        List<String> response = new ArrayList<>();
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if(noteOptional.isPresent()){
            noteRepository.delete(noteOptional.get());
            response.add("The note was successfully deleted");
        }
        return response;
    }

    @Override
    @Transactional
    public List<String> updateNote(NoteDto noteDto){
        List<String> response = new ArrayList<>();
        Optional<Note> noteOptional = noteRepository.findById(noteDto.getId());
        if(noteOptional.isPresent()){
            Note note = noteOptional.get();
            note.setBody(noteDto.getBody());
            response.add("The note was successfully updated");
        }
        return response;
    }

    @Override
    @Transactional
    public List<NoteDto> getAllNotesByUserId(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);
        if(userOptional.isPresent()){
            List<Note> noteList = noteRepository.findAllNotesByUser(userOptional.get());
            return noteList.stream().map(note -> new NoteDto(note)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    @Transactional
    public Optional<NoteDto> getNoteById(Long noteId){
        Optional<Note> noteOptional = noteRepository.findById(noteId);
        if(noteOptional.isPresent()){
            return Optional.of(new NoteDto(noteOptional.get()));
        }
        return Optional.empty();
    }
}
