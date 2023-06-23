package com.jh.SomsomMarket.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@RequiredArgsConstructor
@Getter @Setter
@Entity
@SequenceGenerator(name="SEQ_WISHLIST", sequenceName="WISHLIST_ID_SEQ", allocationSize=1)
public class Wishlist {
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_WISHLIST")
    @Column(name = "wishlist_id")
    private long wishlistId;
    @Column(name = "account_id")
    private String accountId;
    @Column(name = "item_id")
    private long itemId;
}
