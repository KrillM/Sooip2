package com.sooip.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "basket_item")
public class BasketItem extends BaseEntity{
    @Id
    @Column(name="basket_item_id")
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="basket_id")
    private Basket basket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_id")
    private Item item;

    private int count;

    public static BasketItem createBasketItem(Basket basket, Item item, int count){
        BasketItem basketItem = new BasketItem();
        basketItem.setBasket(basket);
        basketItem.setItem(item);
        basketItem.setCount(count);
        return basketItem;
    }
    public void addCount(int count){
        this.count+=count;
    }

    public void updateCount(int count){
        this.count = count;
    }
}
