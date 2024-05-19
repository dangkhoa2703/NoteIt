package com.noteit.secutity;

import com.noteit.entity.Note;
import com.noteit.entity.User;
import com.noteit.service.NoteService;
import com.noteit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * contain one method to check if the current User is authorized to see a note.
 */
@Component
public class AuthorizeCheck {

    private final UserService userService;
    private final NoteService noteService;

    /**
     * Instantiates a new Authorize check.
     *
     * @param userService the user service
     * @param noteService the note service
     */
    @Autowired
    public AuthorizeCheck(UserService userService, NoteService noteService){
        this.userService = userService;
        this.noteService =  noteService;
    }

    /**
     * Check current whether current user is the author of or have access to the note.
     *
     * @param noteId the note id
     * @return the boolean
     */
    public boolean check(int noteId) {
        int userId = userService.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        Note note = noteService.findById(noteId);

        List<Integer> sharedUserId = note.getSharedUser().stream().map(User::getId).toList();

        Boolean isAuthor = Objects.equals(userId, note.getAuthor().getId());
        Boolean isShared = sharedUserId.contains(userId);
        return (isAuthor || isShared);
    }
}
