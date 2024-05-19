package com.noteit.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * User entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToMany( fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST,CascadeType.DETACH,
            CascadeType.MERGE,CascadeType.REFRESH})
    @JoinTable(
            name = "shared_note",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "note_id")
    )
    private List<Note> sharedNote;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private List<Note> createdNote;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * Add shared note.
     *
     * @param theNote the note
     */
    public void addSharedNote(Note theNote){

        if(sharedNote == null){
            sharedNote = new ArrayList<>();
        }
        sharedNote.add(theNote);
    }

    /**
     * Add created note.
     *
     * @param theNote the note
     */
    public void addCreatedNote(Note theNote){

        if(createdNote == null){
            createdNote = new ArrayList<>();
        }

        createdNote.add(theNote);
    }

}
