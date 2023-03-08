package com.sooip.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BasketOrderDto {
    private Long cartItemId;

    private List<BasketOrderDto> basketOrderDtoList;
}
