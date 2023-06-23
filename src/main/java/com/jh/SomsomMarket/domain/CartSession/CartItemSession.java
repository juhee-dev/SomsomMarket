package com.jh.SomsomMarket.domain.CartSession;

import com.jh.SomsomMarket.domain.item.SomsomItem;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@SuppressWarnings("serial")
@Getter @Setter
public class CartItemSession implements Serializable {

    private SomsomItem item;
    private int quantity;
    private boolean inStock;

    public void incrementQuantity() {
        quantity++;
    }
}
