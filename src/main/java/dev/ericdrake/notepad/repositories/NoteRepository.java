package dev.ericdrake.notepad.repositories;

import dev.ericdrake.notepad.entities.Note;
import dev.ericdrake.notepad.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllNotesByUser(User user);
}
