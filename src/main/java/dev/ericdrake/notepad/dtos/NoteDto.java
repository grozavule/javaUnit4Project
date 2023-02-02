package dev.ericdrake.notepad.dtos;

import dev.ericdrake.notepad.entities.Note;
import dev.ericdrake.notepad.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto implements Serializable {
    private Long id;
    private String body;
    private User user;

    public NoteDto(Note note){
        this.id = note.getId();
        this.body = note.getBody();
    }
}
