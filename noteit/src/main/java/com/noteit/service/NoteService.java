package com.noteit.service;

import com.noteit.dto.NoteRepresentation;
import com.noteit.entity.Note;
import com.noteit.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/**
 * The interface Note service, bind logic of note to dao methods.
 */
// bind DAO method to app
public interface NoteService {

    /**
     * Find all list.
     *
     * @return the list
     */
    List<Note> findAll();

    /**
     * Find by id note.
     *
     * @param theId the the id
     * @return the note
     */
    Note findById(int theId);

    /**
     * Save note.
     *
     * @param theNote the the note
     * @return the note
     */
    Note save(Note theNote);

    /**
     * Edit note.
     *
     * @param noteRep the note representation
     * @param id      the id
     * @return the note
     */
    Note edit(NoteRepresentation noteRep, int id);

    /**
     * Delete by id.
     *
     * @param theId the the id
     */
    void deleteById(int theId);

    /**
     * Is author boolean.
     *
     * @param noteId the note id
     * @return the boolean
     */
    Boolean isAuthor(int noteId);
}
