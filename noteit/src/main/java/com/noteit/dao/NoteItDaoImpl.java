package com.noteit.dao;

import com.noteit.entity.User;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QueryHints;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NoteItDaoImpl implements NoteItDao {

    @Autowired
    private EntityManager entityManager;

    public NoteItDaoImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void deleteByUserIdAndNoteId(int theNoteId, int theUserId) {

        // delete record with given noteId and userid
        Query theQuery = entityManager.createQuery(
                "DELETE FROM UserNote un WHERE un.noteId=:noteId AND un.userId=:userId" );
        theQuery.setParameter("noteId",theNoteId);
        theQuery.setParameter("userId",theUserId);
        theQuery.executeUpdate();
    }

//  load user with createdNote and sharedNote
    @Override
    public User findUserByIdJoinFetch(int userId) {
        try {
            String jpql = "select u from User u "
                    + "LEFT JOIN FETCH u.createdNote "
                    + "where u.id = :data";

            User user = entityManager
                    .createQuery(jpql, User.class)
                    .setParameter("data", userId)
                    .getSingleResult();

            // create query
            jpql = "select u from User u "
                    + "LEFT JOIN FETCH u.sharedNote "
                    + "where u in :user";

            user = entityManager
                    .createQuery(jpql, User.class)
                    .setParameter("user", user)
                    .getSingleResult();
            return user;
        } catch(NoResultException e) {
            return null;
        }

    }

}