package com.jh.SomsomMarket.repository;


import com.jh.SomsomMarket.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    void deleteByAccountId(String id);
    List<Order> findByAccountId(String id);
}
