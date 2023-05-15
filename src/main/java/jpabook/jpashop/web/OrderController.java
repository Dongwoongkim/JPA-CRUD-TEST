package jpabook.jpashop.web;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.form.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String orderForm(Model model) {
        List<Member> members = memberService.findAll();
        List<Item> items = itemService.findAll();
        model.addAttribute("items", items);
        model.addAttribute("members", members);
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam Long memberId, @RequestParam Long itemId, @RequestParam int count) {
        orderService.save(memberId, itemId, count);
        return "redirect:/";
    }

    @GetMapping("/orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {
        List<Order> orders = orderService.findBySearch(orderSearch);
        model.addAttribute("orders", orders);
        return "order/orderList";
    }

    @PostMapping("/orders/{orderId}/cancel")
    public String orderCancel(@PathVariable Long orderId) {
        orderService.cancel(orderId);
        return "redirect:/orders";
    }
}
