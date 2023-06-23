package com.jh.SomsomMarket.dao;

import com.jh.SomsomMarket.domain.item.SomsomItem;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class SomsomItemDao {

    private final EntityManager em;

    @Transactional
    public SomsomItem insertSomsomItem(SomsomItem somsomItem) throws DataAccessException {
        em.persist(somsomItem);
        return somsomItem;
    }

    @Transactional
    public void updateSomsomItem(SomsomItem somsomItem) throws DataAccessException {
        em.merge(somsomItem);
    }
    @Transactional
    public void deleteSomsomItem(SomsomItem somsomItem) {
        if (em.contains(somsomItem)) {
            em.remove(somsomItem);
        } else {
            em.remove(em.merge(somsomItem));
        }
    }

    public SomsomItem findOne(Long id) {
        return em.find(SomsomItem.class, id);
    }

    public List<SomsomItem> findAll() {
        return em.createQuery("select s from SomsomItem s", SomsomItem.class)
                .getResultList();
    }

    public void updateAddItemWishCount(Long itemId) {
        Query query = em.createNativeQuery("Update Item i SET i.wish_count = i.wish_count+1 Where i.item_id = ?1");

        query.setParameter(1, itemId);
        query.executeUpdate();
    }

    public void updateDeleteItemWishcount(Long itemId) {
        Query query = em.createNativeQuery("Update Item i SET i.wish_count = i.wish_count-1 Where i.item_id = ?1");
        query.setParameter(1, itemId);
        query.executeUpdate();
    }
}
