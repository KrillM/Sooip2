package com.sooip.dto;

import com.sooip.constant.ItemCategory;
import com.sooip.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemSearchDto {
    private String searchDateType;
    private ItemSellStatus searchSellStatus;
    private ItemCategory searchCategory;
    private String searchBy;
    private String searchQuery = "";
}
