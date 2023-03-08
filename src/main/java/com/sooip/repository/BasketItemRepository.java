package com.sooip.repository;

import com.sooip.dto.BasketDetailDto;
import com.sooip.entity.Basket;
import com.sooip.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
    BasketItem findByBasketIdAndItemId(Long basketId, Long itemId);
    @Query("select new com.sooip.dto.BasketDetailDto(ci.id, i.itemName, i.price, ci.count, im.imageUrl) " +
            "from BasketItem ci, ItemImage im "+
            "join ci.item i " +
            "where ci.basket.id = :basketId " +
            "and im.item.id = ci.item.id " +
            "and im.repImgYn = 'Y' " +
            "order by ci.registerTime desc")
    List<BasketDetailDto> findBasketDetailDtoList(Long basketId);
}
