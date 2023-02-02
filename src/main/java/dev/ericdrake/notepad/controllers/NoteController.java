package dev.ericdrake.notepad.controllers;

import dev.ericdrake.notepad.dtos.NoteDto;
import dev.ericdrake.notepad.services.NoteService;
import dev.ericdrake.notepad.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/notes")
public class NoteController {
    @Autowired
    private UserService userService;
    @Autowired
    private NoteService noteService;

    @GetMapping("/user/{userId}")
    public List<NoteDto> getAllNotesByUser(@PathVariable Long userId){
        return noteService.getAllNotesByUserId(userId);
    }

    @PostMapping("/user/{userId}")
    public List<String> addNote(@RequestBody NoteDto noteDto, @PathVariable Long userId){
        List<String> response = new ArrayList<>();
        noteService.addNote(noteDto, userId);
        response.add("Note successfully created");
        return response;
    }

    @GetMapping("/{noteId}")
    public Optional<NoteDto> getNoteById(@PathVariable Long noteId){
        return noteService.getNoteById(noteId);
    }

    @DeleteMapping("/{noteId}")
    public List<String> deleteNote(@PathVariable Long noteId){
        List<String> response = new ArrayList<>();
        noteService.deleteNote(noteId);
        response.add("Note was successfully deleted");
        return response;
    }

    @PutMapping("/{noteId}")
    public List<String> updateNote(NoteDto noteDto, @PathVariable Long noteId){
        List<String> response = new ArrayList<>();
        noteService.updateNote(noteDto);
        response.add("Note was successfully updated");
        return response;
    }
}
