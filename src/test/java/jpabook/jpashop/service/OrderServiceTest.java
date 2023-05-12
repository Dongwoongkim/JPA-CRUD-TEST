package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
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

        // given
        Member member = createMember();
        Item item  = createBook("jpa", 34000, 10);

        int orderCount = 2;

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // then
        Order order = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, order.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 함.", 1, order.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량 ", 34000 * orderCount, order.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 함", 10-orderCount, item.getStockQuantity());
    }


    @Test
    public void 상품주문_재고수량초과() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("jpa", 34000, 3);
        int orderCount = 4;

        // when
        Assertions.assertThatThrownBy(() -> orderService.order(member.getId(), item.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);
    }

    @Test
    public void 주문취소() throws Exception {
        // given
        Member member = createMember();
        Item item = createBook("JPA1", 35000, 5);

        // when
        Long orderId = orderService.order(member.getId(), item.getId(), 4);
        Order completedOrder = orderRepository.findOne(orderId);

        assertEquals("주문 후 재고량은 count 만큼 줄어 있어야 함",item.getStockQuantity(),1);
        assertEquals("주문 후 상태는 ORDER",completedOrder.getStatus(), OrderStatus.ORDER);

        // 주문 취소
        orderService.cancelOrder(orderId);


        // then
        assertEquals("주문 취소 후 상태는 CANCEL",completedOrder.getStatus(), OrderStatus.CANCEL);
        assertEquals("주문 취소 후 재고랑 취소 전 재고랑 같아야 합니다.", 5, item.getStockQuantity());
    }

    private Book createBook(String name, int price, int count) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(count);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("전주", "금암", "명륜1길"));
        em.persist(member);
        return member;
    }
}