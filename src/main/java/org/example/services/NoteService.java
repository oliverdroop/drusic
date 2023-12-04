package org.example.services;

import lombok.Getter;
import lombok.Setter;
import org.example.Note;
import org.example.Waveform;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.example.Pitch.*;

@Getter
@Setter
@Service
public class NoteService {

    private List<Note> notes = new ArrayList<>();

    public void addNote(Note note) {
        notes.add(note);
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }

    public void removeNotes(List<Note> notesToRemove) {
        notes.removeAll(notesToRemove);
    }
}
