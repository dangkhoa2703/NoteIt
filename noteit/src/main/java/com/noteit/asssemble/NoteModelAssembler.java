package com.noteit.asssemble;

import com.noteit.controller.NoteItController;
import com.noteit.dto.NoteRepresentation;
import com.noteit.entity.Note;
import com.noteit.secutity.AuthorizeCheck;
import com.noteit.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


/**
 * The type Note model assembler, contains one method to model EntityModel with NoteRepresentation type.
 */
@RequiredArgsConstructor
@Component
public class NoteModelAssembler implements RepresentationModelAssembler<NoteRepresentation, EntityModel<NoteRepresentation>> {

    private final NoteService noteService;
    private final AuthorizeCheck authorizeCheck;

    @Override
    public EntityModel<NoteRepresentation> toModel(NoteRepresentation noteRepresentation) {

        Note note = noteService.findById(noteRepresentation.getId());
        int noteId = noteRepresentation.getId();

        // if user is the author, include a href to edit note
        if(noteService.isAuthor(noteId)){
            return EntityModel.of(noteRepresentation,
            linkTo(methodOn(NoteItController.class).one(noteId)).withSelfRel(),
                    linkTo(methodOn(NoteItController.class).editNote(noteRepresentation,noteId)).withRel("edit"),
                    linkTo(methodOn(NoteItController.class).delete(noteId)).withRel("delete"),
                    linkTo(methodOn(NoteItController.class).share(noteId)).withRel("share"),
                    linkTo(methodOn(NoteItController.class).all()).withRel("notes"));
        }

        return EntityModel.of(noteRepresentation,
                linkTo(methodOn(NoteItController.class).one(noteRepresentation.getId())).withSelfRel(),
                linkTo(methodOn(NoteItController.class).delete(noteId)).withRel("delete"),
                linkTo(methodOn(NoteItController.class).all()).withRel("notes"));
    }

}

