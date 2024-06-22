package com.noteit.service;


import com.noteit.entity.Note;
import com.noteit.entity.User;
import com.noteit.exception_handler.custome_exception.UserNameAlreadyExistsException;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;


/**
 * The interface User service.
 */
public interface UserService extends UserDetailsService {

    /**
     * Find by username user.
     *
     * @param username the username
     * @return the user
     */
    User findByUsername(String username);

    /**
     * Save user.
     *
     * @param theUser the the user
     * @return the user
     */
    User save(User theUser) throws UserNameAlreadyExistsException;

    /**
     * Find all list.
     *
     * @return the list
     */
    List<User> findAll();

    /**
     * Find by id user.
     *
     * @param theId the the id
     * @return the user
     */
    User findById(int theId);

    /**
     * Delete by id.
     *
     * @param theId the the id
     */
    void deleteById(int theId);

    /**
     * Find shared note list.
     *
     * @param theId the the id
     * @return the list
     */
    List<Note> findSharedNote(int theId);

    /**
     * Find all note list.
     *
     * @param theId the the id
     * @return the list
     */
    List<Note> findAllNote(int theId);

}
