package com.jh.SomsomMarket.controller.Order;

import com.jh.SomsomMarket.domain.Account;
import com.jh.SomsomMarket.domain.OrderItem;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class OrderRequest {
    private Account account;
    private LocalDate orderDate;
    private String name;
    private String address;
    private String zipcode;
    private List<OrderItem> orderItems = new ArrayList<>();
}
