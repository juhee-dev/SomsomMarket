package com.jh.SomsomMarket.domain.item;

import com.jh.SomsomMarket.domain.ItemStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@SequenceGenerator(name="SEQ_ITEM", sequenceName="ITEM_ID_SEQ", allocationSize=1)
@Getter
@Setter
public abstract class Item {

    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQ_ITEM")
    @Column(name = "item_id")
    private Long id;

    @Column(name = "seller_id")
    private String sellerId;
    private String title;
    private String description;
    private int price;
    @Column(name = "wish_count")
    private int wishCount;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @Column(name = "img_name")
    private String imgName;
    @Column(name = "img_path")
    private String imgPath;
}
