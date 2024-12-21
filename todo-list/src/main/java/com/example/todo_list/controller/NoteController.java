package com.example.todo.controller;

import com.example.todo.model.Note;
import com.example.todo.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<Note> getAllNotes() {
        return noteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable Long id) {
        return noteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Note createNote(@Valid @RequestBody Note note) {
        return noteService.save(note);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> updateNote(
            @PathVariable Long id,
            @Valid @RequestBody Note updatedNote) {
        return noteService.findById(id)
                .map(existingNote -> {
                    existingNote.setTitle(updatedNote.getTitle());
                    existingNote.setContent(updatedNote.getContent());
                    noteService.save(existingNote);
                    return ResponseEntity.ok(existingNote);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable Long id) {
        if (noteService.findById(id).isPresent()) {
            noteService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
