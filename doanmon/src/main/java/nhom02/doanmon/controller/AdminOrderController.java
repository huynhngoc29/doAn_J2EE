package nhom02.doanmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import nhom02.doanmon.entity.Payment;
import nhom02.doanmon.repository.PaymentRepository;

import java.util.List;

@Controller
@RequestMapping("/admin/orders")
public class AdminOrderController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping
    public String listOrders(Model model) {
        List<Payment> orders = paymentRepository.findAll();
        // sort by newest
        orders.sort((a,b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));

        boolean changed = false;
        for (Payment p : orders) {
            if (!p.isAdminRead()) {
                p.setAdminRead(true);
                changed = true;
            }
        }
        if (changed) {
            paymentRepository.saveAll(orders);
        }

        model.addAttribute("orders", orders);
        model.addAttribute("pendingOrderCount", 0);
        return "admin/orders/list";
    }

    @PostMapping("/update-status")
    public String updateStatus(@RequestParam Long orderId, @RequestParam String status) {
        Payment order = paymentRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            order.setUserRead(false);
            paymentRepository.save(order);
        }
        return "redirect:/admin/orders";
    }
}
