package org.example.services;

import lombok.Getter;
import org.example.Note;
import org.example.Waveform;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static org.example.Pitch.*;
import static org.example.Pitch.EN_4;

@Getter
@Service
public class NoteService {

    private List<Note> notes = new ArrayList<>();

    public void addNote(Note note) {
        notes.add(note);
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }

    @PostConstruct
    private void createSampleNotes() {
        final double amplitude = 0.5;

        addNote(new Note(0, 4, AN_4.freq(), amplitude, Waveform.SINE));
        addNote(new Note(1, 4, CS_5.freq(), amplitude, Waveform.TRIANGLE));
        addNote(new Note(2, 4, EN_5.freq(), amplitude, Waveform.SQUARE));

        addNote(new Note(4, 4.5, AN_3.freq(), amplitude, Waveform.SINE));
        addNote(new Note(4.5, 5, BN_3.freq(), amplitude, Waveform.SINE));
        addNote(new Note(5, 5.5, CS_4.freq(), amplitude, Waveform.SINE));
        addNote(new Note(5.5, 6, DN_4.freq(), amplitude, Waveform.SINE));
        addNote(new Note(6, 6.5, EN_4.freq(), amplitude, Waveform.SINE));
        addNote(new Note(6.5, 7, FS_4.freq(), amplitude, Waveform.SINE));
        addNote(new Note(7, 7.5, GS_4.freq(), amplitude, Waveform.SINE));
        addNote(new Note(7.5, 8, AN_4.freq(), amplitude, Waveform.SINE));

        addNote(new Note(9, 10, AN_4.freq(), amplitude, Waveform.SINE));
        addNote(new Note(9, 10, CS_4.freq(), amplitude, Waveform.SINE));
        addNote(new Note(9, 10, EN_4.freq(), amplitude, Waveform.SINE));
    }
}
