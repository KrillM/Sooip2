package com.sooip.repository;

import com.sooip.dto.ItemSearchDto;
import com.sooip.dto.MainItemDto;
import com.sooip.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {
    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getCateItemBeverage(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getCateItemCandy(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getCateItemCookie(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getCateItemRamen(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getCateItemRetort(ItemSearchDto itemSearchDto, Pageable pageable);
    Page<MainItemDto> getCateItemOthers(ItemSearchDto itemSearchDto, Pageable pageable);
}
