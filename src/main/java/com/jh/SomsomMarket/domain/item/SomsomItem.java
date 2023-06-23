package com.jh.SomsomMarket.domain.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataAccessException;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("SOMSOM")
@Getter @Setter
public class SomsomItem extends Item {

    @Column(name = "stock_quantity")
    private int stockQuantity;

    //==비즈니스 로직==//
    /**
     * stock 증가
     */
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    /**
     * stock 감소
     */
    public void removeStock(int quantity) {
        int restStock = this.stockQuantity - quantity;
        if (restStock < 0) {
            //throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }

    public boolean isItemInStock(Long itemId) throws DataAccessException {
        return (stockQuantity > 0);
    }

    public boolean existsByItemIdAndQuantityGreaterThan(Long itemId, int qty) {
        return (stockQuantity > qty);
    }
}
