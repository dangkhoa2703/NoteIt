package com.noteit.service;

import com.noteit.dao.NoteItDao;
import com.noteit.dao.UserRepository;
import com.noteit.entity.Note;
import com.noteit.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final NoteItDao noteItDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    @Override
    public User save(User theUser) {
        return userRepository.save(theUser);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(int theId) {
        return userRepository.findById(theId)
                .orElseThrow(() -> new UsernameNotFoundException("User id not found"));
    }

    @Override
    public void deleteById(int theId) {
        userRepository.deleteById(theId);
    }

    @Override
    public List<Note> findSharedNote(int theId) {

        User user = findById(theId);

        return user.getSharedNote();
    }

    @Override
    public List<Note> findAllNote(int theId) {

        User user = noteItDao.findUserByIdJoinFetch(theId);

        List<Note> allNote = new ArrayList<>();

        allNote.addAll(user.getCreatedNote());
        allNote.addAll(user.getSharedNote());

        return allNote;
    }
}
