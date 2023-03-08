package com.sooip.service;

import com.sooip.constant.ItemSellStatus;
import com.sooip.dto.BasketItemDto;
import com.sooip.entity.Basket;
import com.sooip.entity.BasketItem;
import com.sooip.entity.Item;
import com.sooip.entity.Member;
import com.sooip.repository.BasketItemRepository;
import com.sooip.repository.ItemRepository;
import com.sooip.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class BasketServiceTest {
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    BasketService basketService;
    @Autowired
    BasketItemRepository basketItemRepository;

    public Item saveItem(){
        Item item = new Item();
        item.setItemName("테스트상품");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setItemDetail("테스트 상품입니다.");
        item.setPrice(1000);
        item.setStock(100);
        return itemRepository.save(item);
    }
    public Member saveMember(){
        Member member = new Member();
        member.setEmail("test@test.com");
        return memberRepository.save(member);
    }
    @Test
    @DisplayName("장바구니 담기 테스트")
    public void addBasket(){
        Item item = saveItem();
        Member member = saveMember();
        BasketItemDto basketItemDto = new BasketItemDto();
        basketItemDto.setCount(5);
        basketItemDto.setItemId(item.getId());

        Long basketItemId = basketService.addBasket(basketItemDto, member.getEmail());

        BasketItem basketItem = basketItemRepository.findById(basketItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), basketItem.getItem().getId());
        assertEquals(basketItemDto.getCount(), basketItem.getCount());
    }
}