package com.sooip.entity;

import com.sooip.constant.ItemCategory;
import com.sooip.constant.ItemSellStatus;
import com.sooip.dto.ItemFormDto;
import com.sooip.exception.OutOfStockException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="item")
@Getter
@Setter
@ToString
public class Item extends BaseEntity{
    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, length = 50)
    private String itemName;

    @Column(nullable = false, name="price")
    private int price;

    @Column(nullable = false)
    private int stock;

    @Lob
    @Column(nullable = false)
    private String itemDetail;

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @Enumerated(EnumType.STRING)
    private ItemCategory itemCategory;

//    private LocalDateTime registerTime;
//    private LocalDateTime updateTime;

    @ManyToMany
    @JoinTable(
            name = "member_item",
            joinColumns = @JoinColumn(name="member_id"),
            inverseJoinColumns = @JoinColumn(name="item_id")
    )
    private List<Member> memberList;

    public void updateItem(ItemFormDto itemFormDto){
        this.itemName = itemFormDto.getItemName();
        this.price = itemFormDto.getPrice();
        this.stock = itemFormDto.getStock();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
        this.itemCategory = itemFormDto.getItemCategory();
    }

    public void removeStock(int stockNumber){
        int restStock = this.stock - stockNumber; // 10,  5 / 10, 20
        if(restStock<0){
            throw new OutOfStockException("죄송합니다. 재고가 부족합니다.(현재 수량: "+this.stock+")");
        }
        this.stock = restStock; // 5
    }
    public void addStock(int stock){
        this.stock += stock;
    }
}
