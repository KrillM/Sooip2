package com.sooip.entity;

import com.sooip.dto.MemberFormDto;
import com.sooip.repository.BasketRepository;
import com.sooip.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class BasketTest {
    @Autowired
    BasketRepository basketRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @PersistenceContext
    EntityManager entityManager;

    public Member createMember(){
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("우영우");
        memberFormDto.setAddress("서울시");
        memberFormDto.setAddressDetail("마포구");
        memberFormDto.setAddressInfo("합정동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("Basket Member Entity Mapping check list Test")
    public void findBasketNMemberTest(){
        Member member = createMember();
        memberRepository.save(member);
        Basket basket = new Basket();
        basket.setMember(member);
        basketRepository.save(basket);

        entityManager.flush();
        entityManager.clear();

        Basket saveBasket = basketRepository.findById(basket.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(saveBasket.getMember().getId(), member.getId());
    }
}