package com.noteit.controller;

import com.noteit.dao.NoteItDao;
import com.noteit.dao.UserRepository;
import com.noteit.dto.NoteRepresentation;
import com.noteit.dto.ShareUserRepresentation;
import com.noteit.entity.Note;
import com.noteit.entity.User;
import com.noteit.exception_handler.CustomErrorResponse;
import com.noteit.secutity.AuthorizeCheck;
import com.noteit.asssemble.NoteModelAssembler;
import com.noteit.service.NoteService;
import com.noteit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * App main controller.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/noteit")
public class NoteItController {

    private final UserService userService;
    private final NoteService noteService;
    private final NoteItDao noteItDao;
    private final NoteModelAssembler assembler ;
    private final AuthorizeCheck authorizeCheck;
    private final UserRepository userRepository;


    /**
     * Request one note with given id.
     *
     * @param id the note id
     */
    @PreAuthorize("@authorizeCheck.check(#id)")
    @GetMapping("/{id}")
    public EntityModel<NoteRepresentation> one(@PathVariable int id) {

        Note note = noteService.findById(id);
        NoteRepresentation noteRep = new NoteRepresentation(note);

        return assembler.toModel(noteRep);
    }

    /**
     * Request all current user note.
     */
    @GetMapping
    public CollectionModel<EntityModel<NoteRepresentation>> all() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.findByUsername(username);

        List<EntityModel<NoteRepresentation>> notes = userService
                .findAllNote(user.getId())
                .stream()
                .map(NoteRepresentation::new)
                .map(assembler::toModel)
                .toList();

        return CollectionModel.of(notes,
                linkTo(methodOn(NoteItController.class).all()).withSelfRel());
    }

    /**
     * Create new note.
     *
     * @param noteRep the note
     */
    @PostMapping
    public ResponseEntity<?> newNote(@RequestBody NoteRepresentation noteRep) {

        User user = userService.findByUsername(
                SecurityContextHolder.getContext().getAuthentication().getName());
        Note note = new Note();
        note.setSubject(noteRep.getSubject());
        note.setContent(noteRep.getContent());

        // set author
        note.setAuthor(user);
        Note savedNote = noteService.save(note);

        NoteRepresentation noteRepTemp = new NoteRepresentation(savedNote);
        EntityModel<NoteRepresentation> entityModel = assembler.toModel(noteRepTemp);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * Edit a note.
     *
     * @param noteRep the note representation
     * @param id   the note id
     */
    @PreAuthorize("@authorizeCheck.authorCheck(#id)")
    @PutMapping("/{id}")
    public ResponseEntity<?> editNote(@RequestBody NoteRepresentation noteRep, @PathVariable int id) throws HttpClientErrorException {

        if(noteRep.getContent() == null || noteRep.getSubject() == null){
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST,"Missing note subject and/or note content");
        }
        Note editedNote = noteService.edit(noteRep, id);
        NoteRepresentation noteRepTemp = new NoteRepresentation(editedNote);

        EntityModel<NoteRepresentation> entityModel = assembler.toModel(noteRepTemp);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /**
     * Delete a note.
     *
     * @param id the id
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {

        EntityModel<Link> entityModel = EntityModel.of(
                linkTo(methodOn(NoteItController.class).all()).withRel("notes"));

        // current user
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userService.findByUsername(currentUsername);
        Note toDeleteNote = noteService.findById(id);
        String noteAuthor = toDeleteNote.getAuthor().getUsername();

        // in case user is the author
        if (Objects.equals(currentUsername, noteAuthor)) {
            noteService.deleteById(id);
        }
        // in case user is not the author
        else {
            noteItDao.deleteByUserIdAndNoteId(id, currentUser.getId());
        }

//        return ResponseEntity.noContent().build();
        return ResponseEntity
                .created(linkTo(methodOn(NoteItController.class).all()).toUri())
                .body(entityModel);
    }

    /**
     * Get a list of user to share note with.
     *
     * @param noteId the note id
     * @return list of user with share note link
     */
    @GetMapping("/share/{noteId}")
    public CollectionModel<EntityModel<ShareUserRepresentation>> share(@PathVariable int noteId) {

        Note note = noteService.findById(noteId);
        List<User> allUser = userService.findAll();

        List<Integer> unsharedUserIdList = new ArrayList<>(userService.findAll().stream().map(User::getId).toList());

        List<Integer> sharedUserIdList = noteService.findById(noteId).getSharedUser()
                .stream().map(User::getId).toList();

        // remove the shared user id and author id out of the list
        unsharedUserIdList.removeAll(sharedUserIdList);
        unsharedUserIdList.remove(note.getAuthor().getId());

        List<ShareUserRepresentation> shareUserRep = unsharedUserIdList
                .stream()
                .map(userId -> new ShareUserRepresentation(userService.findById(userId))).toList();

        List<EntityModel<ShareUserRepresentation>> allUsernameEntityModel =
                shareUserRep
                        .stream()
                        .map(userRep -> EntityModel.of(userRep,
                                linkTo(methodOn(NoteItController.class)
                                        .share(noteId,userRep.getUsername()))
                                        .withRel("share with")))
                        .toList();

        return CollectionModel.of(allUsernameEntityModel,
                linkTo(methodOn(NoteItController.class).all()).withSelfRel());
    }

    /**
     * Share one note to a user.
     *
     * @param noteId   the note id
     * @param username the username
     */
    @PutMapping("/share/{noteId}/{username}")
    public ResponseEntity<?> share(@PathVariable int noteId, @PathVariable String username) {

        Note note = noteService.findById(noteId);

        List<Integer> sharedUserIdList =  note.getSharedUser().stream().map(User::getId).toList();

        if(!sharedUserIdList.contains(noteId)) {
            note.addSharedUser(userService.findByUsername(username));
            noteService.save(note);
        }

        return ResponseEntity
                .created(linkTo(methodOn(NoteItController.class).one(noteId)).toUri())
                .body(assembler
                        .toModel(new NoteRepresentation(note)));
    }
}
