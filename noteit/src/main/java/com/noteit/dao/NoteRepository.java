package com.noteit.dao;

import com.noteit.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * The interface Note repository.
 */
public interface NoteRepository extends JpaRepository<Note, Integer> {
}
