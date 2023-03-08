package com.sooip.controller;

import com.sooip.dto.BasketDetailDto;
import com.sooip.dto.BasketItemDto;
import com.sooip.dto.BasketOrderDto;
import com.sooip.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BasketController {
    private final BasketService basketService;

    @PostMapping(value = "/basket")
    public @ResponseBody
    ResponseEntity order(@RequestBody @Valid BasketItemDto basketItemDto,
                         BindingResult bindingResult, Principal principal){
        if(bindingResult.hasErrors()){
            StringBuilder sb = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for(FieldError fieldError: fieldErrors){
                sb.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(sb.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName();
        Long basketItemId;
        try{
            basketItemId = basketService.addBasket(basketItemDto, email);
        } catch (Exception e){
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(basketItemId, HttpStatus.OK);
    }
    @GetMapping(value = "/basket")
    public String orderHistory(Principal principal, Model model){
        List<BasketDetailDto> basketDetailDtoList = basketService.getBasketList(principal.getName());
        System.out.println(basketDetailDtoList.size());
        model.addAttribute("basketItems",basketDetailDtoList);
        return "/basket/basketList";
    }

    @PatchMapping(value = "/basketItem/{cartItemId}")
    public @ResponseBody ResponseEntity updateCartItem(@PathVariable("cartItemId") Long cartItemId,
                                                       int count, Principal principal) {
        System.out.println(cartItemId);
        if (count <= 0) {
            return new ResponseEntity<String>("최소 1개이상 담아주세요.", HttpStatus.BAD_REQUEST);
        } else if (!basketService.validateBasketItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }

        basketService.updateBasketItemCount(cartItemId, count);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }

    @DeleteMapping(value = "/basketItem/{cartItemId}")
    public @ResponseBody ResponseEntity deleteBasketItem(@PathVariable("cartItemId") Long cartItemId,
                                                       Principal principal){
        if (!basketService.validateBasketItem(cartItemId, principal.getName())) {
            return new ResponseEntity<String>("수정권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        basketService.deleteBasketItem(cartItemId);
        return new ResponseEntity<Long>(cartItemId, HttpStatus.OK);
    }
    @PostMapping(value = "/basket/orders")
    public @ResponseBody ResponseEntity orderBasketItem(@RequestBody BasketOrderDto basketOrderDto, Principal principal){
        System.out.println(basketOrderDto.getCartItemId());
        List<BasketOrderDto> basketOrderDtoList = basketOrderDto.getBasketOrderDtoList();

        if(basketOrderDtoList == null || basketOrderDtoList.size() == 0){
            return new ResponseEntity<String>("주문할 상품을 선택해주세요.",HttpStatus.FORBIDDEN);
        }
        for(BasketOrderDto basketOrder : basketOrderDtoList){
            if(!basketService.validateBasketItem(basketOrder.getCartItemId(), principal.getName())){
                return new ResponseEntity<String>("주문 권한이 없습니다.",HttpStatus.FORBIDDEN);
            }
        }
        Long orderId = basketService.orderBasketItem(basketOrderDtoList, principal.getName());

        return new ResponseEntity<Long>(orderId,HttpStatus.OK);
    }
}
