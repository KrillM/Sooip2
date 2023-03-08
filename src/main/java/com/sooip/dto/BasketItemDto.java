package com.sooip.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class BasketItemDto {
    @NotNull(message = "상품 이미지를 넣어주세요")
    private Long itemId;

    @Min(value = 1, message = "최소 1개 이상 필요합니다")
    private int count;
}
