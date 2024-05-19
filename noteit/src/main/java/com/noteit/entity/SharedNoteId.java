package com.noteit.entity;

import java.io.Serializable;


/**
 * because UserNote have a composite key( 2 or more primary keys), the id of UserNote must be separately defined
 */
public class SharedNoteId implements Serializable {

    private int noteId;

    private int userId;

    /**
     * Instantiates a new Shared note id.
     *
     * @param noteId the note id
     * @param userId the user id
     */
    public SharedNoteId(int noteId, int userId) {
        this.noteId = noteId;
        this.userId = userId;
    }
}
