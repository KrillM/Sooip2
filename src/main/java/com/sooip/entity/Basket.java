package com.sooip.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="basket")
@Getter
@Setter
@ToString
public class Basket {
    @Id
    @Column(name="basket_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    public static Basket createBasket(Member member){
        Basket basket = new Basket();
        basket.setMember(member);
        return basket;
    }
}
