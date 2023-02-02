package dev.ericdrake.notepad.services;

import dev.ericdrake.notepad.dtos.NoteDto;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    @Transactional
    List<String> addNote(NoteDto noteDto, Long userId);

    @Transactional
    List<String> deleteNote(Long noteId);

    @Transactional
    List<String> updateNote(NoteDto noteDto);

    @Transactional
    List<NoteDto> getAllNotesByUserId(Long userId);

    @Transactional
    Optional<NoteDto> getNoteById(Long noteId);
}
