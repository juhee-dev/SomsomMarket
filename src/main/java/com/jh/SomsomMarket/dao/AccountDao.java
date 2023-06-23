package com.jh.SomsomMarket.dao;

import com.jh.SomsomMarket.domain.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class AccountDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public Account insertAccount(Account account) throws DataAccessException {
        em.persist(account);
        return account;
    }

    @Transactional
    public Account updateAccount(Account account) throws DataAccessException {
        return em.merge(account);
    }

    @Transactional
    public void updatePassword(String id, String password) throws DataAccessException {
        Query query = em.createQuery(
                "UPDATE Account a SET a.password = :password WHERE a.id = :id");
        query.setParameter("id", id);
        query.setParameter("password", password);
        query.executeUpdate();
    }

    @Transactional
    public void deleteAccount(Account account) {
        if (em.contains(account)) {
            // Persistence Context 영역에 존재하지 않을 경우 예외 발생하므로 존재 여부 확인 후 remove
            em.remove(account);
        } else {
            // 없을 경우, em.merge를 통하여 객체가 Persistence Context 영역에 분리된 상태를
            // Persistence Context 영역에 영속상태로 유지하고 remove 함.
            em.remove(em.merge(account));
        }
    }

    public Account findOne(String id) {
        return em.find(Account.class, id);
    }

    public List<Account> findAll() {
        return em.createQuery("select a from Account a", Account.class)
                .getResultList();
    }

    public List<Account> findByName(String id) {
        return em.createQuery("select a from Account a where a.id = :userId", Account.class)
                .setParameter("userId", id)
                .getResultList();
    }
}
