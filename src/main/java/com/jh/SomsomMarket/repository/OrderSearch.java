package com.jh.SomsomMarket.repository;

import com.jh.SomsomMarket.domain.OrderStatus;
import lombok.Data;

@Data
public class OrderSearch {

    private String accountId; //회원 아이디
    private OrderStatus orderStatus; //주문 상태[PROCESSED, CANCEL]
}
