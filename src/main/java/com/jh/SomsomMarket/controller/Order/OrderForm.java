package com.jh.SomsomMarket.controller.Order;

import com.jh.SomsomMarket.domain.Order;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderForm implements Serializable {

    private final Order order = new Order();
}
