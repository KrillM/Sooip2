package com.sooip.dto;

import com.sooip.entity.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private String itemName;
    private String itemType;
    private int count;
    private int orderPrice;
    private String imageUrl;

    public OrderItemDto(OrderItem orderItem, String imageUrl){
        this.itemName=orderItem.getItem().getItemName();
        this.count=orderItem.getCount();
        this.orderPrice=orderItem.getOrderPrice();
        this.imageUrl=imageUrl;
    }
}
