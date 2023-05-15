package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;


@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Item item = createItem();
        int orderCount = 2;

        //when
        Long orderId = orderService.save(member.getId(), item.getId(), orderCount);

        //then
        Order order = orderRepository.findById(orderId);
        assertEquals("주문한 경우 상태는 ORDER",order.getStatus(),OrderStatus.ORDER);
        assertEquals("주문 가격은 일치 ",order.getTotalPrice(),orderCount * item.getPrice());
        assertEquals("주문수량만큼 재고 감소",item.getStockQuantity(),98);
        assertEquals("주문한 상품 종류 수 ",order.getOrderItems().size(),1);
    }

    @Test
    public void 상품주문취소() throws Exception {
        //given
        Member member = createMember();
        Item item = createItem();
        int orderCount = 2;

        //when
        Long orderId = orderService.save(member.getId(), item.getId(), orderCount);

        //then
        orderService.cancel(orderId);
        Order order = orderRepository.findById(orderId);

        assertEquals("주문한 경우 상태는 CANCEL",order.getStatus(), OrderStatus.CANCEL);
        assertEquals("주문 취소만큼 재고 증가",item.getStockQuantity(),100);
    }

    private Book createItem() {
        Book item = new Book();
        item.setName("ITEM");
        item.setPrice(1000);
        item.setStockQuantity(100);
        item.setIsbn("ISBN1");
        item.setAuthor("AUTHOR1");
        em.persist(item);
        return item;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("KIM");
        member.setAddress(new Address("ANDONG","KK","555"));
        em.persist(member);
        return member;
    }

}