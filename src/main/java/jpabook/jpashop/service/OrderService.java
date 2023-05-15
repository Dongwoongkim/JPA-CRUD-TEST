package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.form.OrderSearch;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long save(Long memberId, Long itemId, int count) {
        Member member = memberRepository.findById(memberId);
        Item item = itemRepository.findById(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.crateOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();
    }
    @Transactional
    public void cancel(Long orderId) {
        Order findOrder = orderRepository.findById(orderId);
        findOrder.cancel();
    }
    public List<Order> findBySearch(OrderSearch orderSearch) {
        return orderRepository.findBySearch(orderSearch);
    }
}
