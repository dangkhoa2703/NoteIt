package com.noteit.entity;

import jakarta.persistence.*;

/**
 * Table which record which note is shared to which user.
 */
@Entity
@Table(name="shared_note")
@IdClass(SharedNoteId.class)
public class SharedNote {

    @Id
    @Column(name="note_id")
    private int noteId;

    @Id
    @Column(name="user_id")
    private int userId;

    /**
     * Instantiates a new Shared note.
     */
    public SharedNote(){};


    /**
     * Sets note id.
     *
     * @param noteId the note id
     */
    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }
}