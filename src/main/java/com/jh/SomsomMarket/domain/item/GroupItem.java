package com.jh.SomsomMarket.domain.item;

import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.DataAccessException;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Date;

@Getter @Setter
@Entity
@DiscriminatorValue(value="GROUP")
public class GroupItem extends Item implements Serializable {

    @Column(name="SALES_TARGET")
    private int salesTarget; //공구 목표액
    @Column(name="SALES_NOW")
    private int salesNow; //현재 모금액
    @Column(name="START_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate; //공구 시작일
    @Column(name="END_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate; //공구 마감일

    @Column(name = "stock_quantity")
    private int stockQuantity;

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

}
