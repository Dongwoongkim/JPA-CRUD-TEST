package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.form.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findById(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findBySearch(OrderSearch orderSearch) {
        String jpql = "select o from Order o join o.member m";

        String memberName = orderSearch.getMemberName();
        OrderStatus orderStatus = orderSearch.getOrderStatus();

        boolean firstCondition = true;

        if (StringUtils.hasText(memberName)) {
            if (firstCondition) {
                jpql += " where";
                firstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        if (orderStatus != null) {
            if (firstCondition) {
                jpql += " where";
                firstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status =: status";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);

        if (orderStatus != null) {
            query = query.setParameter("status", orderStatus);
        }
        if (StringUtils.hasText(memberName)) {
            query = query.setParameter("name", memberName);
        }

        return query.getResultList();
    }

}
