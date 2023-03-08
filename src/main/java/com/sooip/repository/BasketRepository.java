package com.sooip.repository;

import com.sooip.entity.Basket;
import com.sooip.entity.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findByMemberId(Long memberId);
}
