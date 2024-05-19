package com.noteit.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.noteit.entity.Note;
import com.noteit.entity.User;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Note representation.
 */
@Getter
public class NoteRepresentation extends RepresentationModel<NoteRepresentation> {

    @Getter
    private final int id;
    private final String subject;
    private final String content;
    private final String author;
    private final List<String> shareWith;


    /**
     * Instantiates a new Note representation.
     *
     * @param note the note
     */
    public NoteRepresentation(Note note){
        this.id = note.getId();
        this.subject = note.getSubject();
        this.content = note.getContent();
        this.author = note.getAuthor().getUsername();
        if(note.getSharedUser() != null) {
            this.shareWith = note
                    .getSharedUser()
                    .stream()
                    .map(User::getUsername)
                    .toList();
        } else {
            this.shareWith = new ArrayList<String>();
        }
    }

}
