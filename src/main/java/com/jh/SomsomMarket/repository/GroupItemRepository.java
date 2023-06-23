package com.jh.SomsomMarket.repository;

import com.jh.SomsomMarket.domain.ItemStatus;
import com.jh.SomsomMarket.domain.item.GroupItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupItemRepository extends JpaRepository<GroupItem, Long> {

    List<GroupItem> findBySellerIdOrderByStartDate(String sellerId); //사용자ID로 사용자의 그룹아이템 리스트 보여주기

    List<GroupItem> findBySellerIdAndStatus(String sellerId, ItemStatus status);

    //List<GroupItem> findBySellerId(String sellerId);

}
