package com.sooip.service;

import com.sooip.dto.BasketDetailDto;
import com.sooip.dto.BasketItemDto;
import com.sooip.dto.BasketOrderDto;
import com.sooip.dto.OrderDto;
import com.sooip.entity.*;
import com.sooip.repository.BasketItemRepository;
import com.sooip.repository.BasketRepository;
import com.sooip.repository.ItemRepository;
import com.sooip.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BasketService {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    public Long addBasket(BasketItemDto basketItemDto, String email){
        Item item = itemRepository.findById(basketItemDto.getItemId())
                .orElseThrow(EntityExistsException::new);
        Member member = memberRepository.findByEmail(email);

        Basket basket = basketRepository.findByMemberId(member.getId());
        if(basket==null){
            basket=Basket.createBasket(member);
            basketRepository.save(basket);
        }

        BasketItem saveBasketItem = basketItemRepository.findByBasketIdAndItemId(basket.getId(),item.getId());

        if(saveBasketItem!=null){
            saveBasketItem.addCount(basketItemDto.getCount());
            return saveBasketItem.getId();
        }
        else {
            BasketItem basketItem = BasketItem.createBasketItem(basket, item, basketItemDto.getCount());
            basketItemRepository.save(basketItem);
            return basketItem.getId();
        }
    }
    @Transactional(readOnly = true)
    public List<BasketDetailDto> getBasketList(String email){
        List<BasketDetailDto> basketDetailDtoList = new ArrayList<>();

        Member member = memberRepository.findByEmail(email);

        Basket basket = basketRepository.findByMemberId(member.getId());
        if(basket == null){
            return basketDetailDtoList;
        }
        basketDetailDtoList = basketItemRepository.findBasketDetailDtoList(basket.getId());
        return basketDetailDtoList;
    }

    @Transactional(readOnly = true)
    public boolean validateBasketItem(Long cartItemId, String email){
        Member curMember = memberRepository.findByEmail(email);
        BasketItem basketItem = basketItemRepository.findById(cartItemId).
                orElseThrow(EntityExistsException::new);
        Member saveMember = basketItem.getBasket().getMember();

        if(!StringUtils.equals(curMember.getEmail(),saveMember.getEmail())){
            return false;
        }
        return true;
    }
    public void updateBasketItemCount(Long cartItemId, int count){
        BasketItem cartItem = basketItemRepository.findById(cartItemId).
                orElseThrow(EntityExistsException::new);
        cartItem.updateCount(count);
    }
    public void deleteBasketItem(Long cartItemId){
        BasketItem basketItem = basketItemRepository.findById(cartItemId).
                orElseThrow(EntityExistsException::new);
        basketItemRepository.delete(basketItem);
    }

    private final OrderService orderService;

    public Long orderBasketItem(List<BasketOrderDto> basketOrderDtoList, String email){
        List<OrderDto> orderDtoList = new ArrayList<>();
        for(BasketOrderDto basketOrderDto : basketOrderDtoList){
            BasketItem basketItem = basketItemRepository.findById(basketOrderDto.getCartItemId())
                    .orElseThrow(EntityExistsException::new);
            OrderDto orderDto = new OrderDto();
            orderDto.setItemId(basketItem.getItem().getId());
            orderDto.setCount(basketItem.getCount());
            orderDtoList.add(orderDto);
        }

        Long orderId = orderService.orders(orderDtoList, email);

        for(BasketOrderDto basketOrderDto : basketOrderDtoList){
            BasketItem basketItem = basketItemRepository.findById(basketOrderDto.getCartItemId())
                    .orElseThrow(EntityExistsException::new);
            basketItemRepository.delete(basketItem);
        }
        return orderId;
    }
}
