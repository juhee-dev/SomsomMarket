package com.jh.SomsomMarket.repository;

import com.jh.SomsomMarket.domain.item.SomsomItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SomsomItemRepository extends JpaRepository<SomsomItem, Long> {
//    Optional<SomsomItem> findById(Long item_id);
    SomsomItem findItemById(Long item_id);
}
