package com.noteit.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Note entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="note")
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="content")
    private String content;

    @Column(name="subject")
    private String subject;


    @ManyToOne(cascade ={CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name="author_id")
    private User author;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(
            name = "shared_note",
            joinColumns = @JoinColumn(name = "note_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> sharedUser;

    /**
     * Add shared user.
     *
     * @param theUser the user
     */
    public void addSharedUser(User theUser) {

        if(sharedUser == null) {
            sharedUser = new ArrayList<>();
        }
        sharedUser.add(theUser);
    }

}