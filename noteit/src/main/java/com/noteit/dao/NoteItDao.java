package com.noteit.dao;

import com.noteit.entity.User;

/**
 * Additional query method for the app.
 */
// remove the record from the join table user_note in DB
public interface NoteItDao {
    /**
     * Delete by user id and note id.
     *
     * @param noteId the note id
     * @param userId the user id
     */
    public void deleteByUserIdAndNoteId(int noteId, int userId);

    /**
     * Load user with the created and shared note.
     *
     * @param id the id
     * @return the user
     */
    public User findUserByIdJoinFetch(int id);

}
