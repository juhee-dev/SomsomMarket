package com.jh.SomsomMarket.repository;

import com.jh.SomsomMarket.domain.item.PersonalItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonalItemRepository extends JpaRepository<PersonalItem, Long> {
    List<PersonalItem> findBySellerIdOrderByStartDateDesc(String sellerId);
    List<PersonalItem> findAllByOrderByStartDateDesc();
}

