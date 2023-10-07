package org.example.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.example.Note;
import org.example.services.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "http://localhost:3000")
public class NoteController {

    private final NoteService noteService;

    private final ObjectMapper objectMapper;

    @GetMapping(value = "/notes")
    public ResponseEntity<List<Note>> getNotes() {
        return ResponseEntity.ok(noteService.getNotes());
    }

    @PostMapping(value = "/notes")
    public ResponseEntity<List<Note>> postNotes(@RequestBody String requestBody) throws JsonProcessingException {
        List<Note> notes = Arrays.stream(objectMapper.readValue(requestBody, Note[].class)).collect(Collectors.toList());
        noteService.setNotes(notes);

        return ResponseEntity.ok(notes);
    }

    @PostMapping(value = "/note")
    public ResponseEntity<List<Note>> postNote(@RequestBody String requestBody) throws JsonProcessingException {
        Note note = objectMapper.readValue(requestBody, Note.class);
        noteService.addNote(note);

        return ResponseEntity.ok(noteService.getNotes());
    }

    @PostMapping(value = "/notes/move")
    public ResponseEntity<Void> moveNotes(@RequestBody String requestBody) {
        System.out.println(requestBody);
        return ResponseEntity.ok().build();
    }
}
