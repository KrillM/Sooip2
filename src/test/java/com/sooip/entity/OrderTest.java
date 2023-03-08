package com.sooip.entity;

import com.sooip.constant.ItemSellStatus;
import com.sooip.repository.ItemRepository;
import com.sooip.repository.MemberRepository;
import com.sooip.repository.OrderItemRepository;
import com.sooip.repository.OrderRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderTest {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ItemRepository itemRepository;
    @PersistenceContext
    EntityManager entityManager;

    public Item createItem(){
        Item item = new Item();
        item.setItemName("Test Item");
        item.setPrice(10000);
        item.setItemDetail("blah blah blah");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStock(100);
        item.setRegisterTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }
    @Test
    @DisplayName("persistence cascade Test")
    public void cascadeTest(){
        Order order = new Order();
        for(int i =0;i<3;i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        orderRepository.saveAndFlush(order); // 주문 추가 -> flush
        entityManager.clear();
        Order saveOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, saveOrder.getOrderItems().size());
    }

    @Autowired
    MemberRepository memberRepository;

    public Order createOrder(){
        Order order = new Order();
        for(int i =0;i<3;i++){
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);
        return order;
    }
    @Test
    @DisplayName("OrphanRemoval Test")
    public void orphanRemovalTest(){
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        entityManager.flush();
    }

    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    @DisplayName("lazy loading Test")
    public void lazyLoadingTest(){
        Order order = this.createOrder();
        Long orderItemID = order.getOrderItems().get(0).getId();
        entityManager.flush();
        entityManager.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemID)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order Class : "+orderItem.getOrder().getClass());
        System.out.println("========================");
        orderItem.getOrder().getOrderDate();
        System.out.println("========================");
    }
}