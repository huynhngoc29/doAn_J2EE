package nhom02.doanmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import nhom02.doanmon.entity.Payment;
import nhom02.doanmon.entity.User;
import nhom02.doanmon.model.Cart;
import nhom02.doanmon.repository.UserRepository;
import nhom02.doanmon.service.PaymentService;

@Controller
public class CheckoutController {

    @Autowired
    private Cart cart;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/checkout")
    public String checkoutForm(Model model, Authentication authentication) {
        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("total", cart.getTotalAmount());
        return "checkout/form";
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam(defaultValue = "COD") String method,
            @RequestParam String address,
            Authentication authentication,
            Model model,
            HttpServletRequest request) {
        User user = null;
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.core.user.OAuth2User) {
                org.springframework.security.oauth2.core.user.OAuth2User oauth2User = (org.springframework.security.oauth2.core.user.OAuth2User) authentication.getPrincipal();
                String email = oauth2User.getAttribute("email");
                user = userRepository.findByUsername(email).orElse(null);
            } else {
                user = userRepository.findByUsername(authentication.getName()).orElse(null);
            }
        }
        Payment payment;
        try {
            payment = paymentService.createPayment(cart, user, method, address);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("cart", cart);
            model.addAttribute("total", cart.getTotalAmount());
            return "checkout/form";
        }

        if ("BANK".equals(method)) {
            // build VNPay redirect URL (include client IP) and clear cart after
            String clientIp = request.getRemoteAddr();
            String redirectUrl = paymentService.createVnpayUrl(payment, clientIp);
            cart.clear();
            return "redirect:" + redirectUrl;
        }

        cart.clear();
        return "checkout/confirmation";
    }

    /**
     * Callback endpoint invoked by VNPay after the user completes bank payment.
     * It updates the payment status and shows a simple result page.
     */
    @GetMapping("/checkout/result")
    public String vnpayReturn(HttpServletRequest request, Model model) {
        String responseCode = request.getParameter("vnp_ResponseCode");
        String txnRef = request.getParameter("vnp_TxnRef");
        Payment payment = null;
        if (txnRef != null) {
            try {
                Long id = Long.valueOf(txnRef);
                payment = paymentService.getPaymentById(id);
                if (payment != null) {
                    if ("00".equals(responseCode)) {
                        payment.setStatus("WAITING_FOR_PICKUP");
                    } else {
                        payment.setStatus("FAILED");
                    }
                    paymentService.save(payment);
                }
            } catch (NumberFormatException ignored) {
            }
        }
        model.addAttribute("payment", payment);
        model.addAttribute("responseCode", responseCode);
        return "checkout/vnpay-result";
    }
}
