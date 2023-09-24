package org.example.controllers;

import lombok.RequiredArgsConstructor;
import org.example.Note;
import org.example.services.NoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @CrossOrigin(value = "http://localhost:3000")
    @GetMapping(value = "/notes")
    public ResponseEntity<List<Note>> getAllNotes() {
        return ResponseEntity.ok(noteService.getNotes());
    }
}
