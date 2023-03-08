package com.sooip.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasketDetailDto {
    private Long cartItemId; // 장바구니 상품 아이디
    private String itemName; // 상품명
    private int price; // 가격
    private int count; // 수량
    private String imageUrl; // 상품이미지 경로

    public BasketDetailDto(Long cartItemId, String itemName,
                           int price, int count, String imageUrl){
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.imageUrl = imageUrl;
        this.count = count;
    }
}
