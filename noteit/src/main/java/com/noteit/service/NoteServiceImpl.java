package com.noteit.service;

import com.noteit.dao.NoteRepository;
import com.noteit.dto.NoteRepresentation;
import com.noteit.entity.Note;
import com.noteit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserService userService;


    @Override
    public List<Note> findAll() {

        return noteRepository.findAll();
    }

    @Override
    public Note findById(int theId) {
        Optional<Note> result = noteRepository.findById(theId);

        Note note = null;

        if (result.isPresent()) {
            note = result.get();
        }
        else {
            throw new RuntimeException("Did not find any note with note id: " + theId);
        }

        return note;
    }

    @Override
    public Note save(Note theNote) {
        return noteRepository.save(theNote);
    }

    @Override
    public Note edit(NoteRepresentation noteRep, int id) {
        Note editedNote = noteRepository.findById(id)
                .map(note -> {
                    note.setSubject(noteRep.getSubject());
                    note.setContent(noteRep.getContent());
                    return noteRepository.save(note);
                })
                .orElseGet(null);
        return noteRepository.save(editedNote);
    }

    @Override
    public void deleteById(int theId) {

        noteRepository.deleteById(theId);
    }

    @Override
    public Boolean isAuthor(int noteId) {

        int userId = userService.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        Note note = findById(noteId);

        return Objects.equals(userId, note.getAuthor().getId());
    }
}