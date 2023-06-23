package com.jh.SomsomMarket.dao;

import com.jh.SomsomMarket.domain.Notes;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class NotesDao {
    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Notes insertNotes(Notes notes) throws DataAccessException {
        em.persist(notes);
        return notes;
    }

    @Transactional
    public Notes updateNotes(Notes notes) throws DataAccessException {
        return em.merge(notes);
    }


}
