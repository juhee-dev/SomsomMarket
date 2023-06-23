package com.jh.SomsomMarket.repository;

import com.jh.SomsomMarket.domain.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Optional<Wishlist> findByAccountIdAndItemId(String id, Long itemId);
    List<Wishlist> findByAccountId(String id);
    void deleteByAccountId(String id);



    void deleteByItemId(Long id);

}
