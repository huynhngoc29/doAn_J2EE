package nhom02.doanmon.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import nhom02.doanmon.entity.Cake;
import nhom02.doanmon.model.Cart;
import nhom02.doanmon.model.CartItem;
import nhom02.doanmon.service.CakeService;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private Cart cart;

    @Autowired
    private CakeService cakeService;

    @GetMapping
    public String viewCart(Model model, org.springframework.security.core.Authentication authentication) {
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return "redirect:/admin/cakes";
        }
        model.addAttribute("cart", cart);
        model.addAttribute("total", cart.getTotalAmount());
        return "cart/view";
    }

    @GetMapping("/add/{id}")
    public String addToCart(@PathVariable Long id, org.springframework.security.core.Authentication authentication) {
        // Prevent ADMIN from adding items to cart
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            return "redirect:/cakes?error=admin_cannot_shop";
        }

        Cake cake = cakeService.findById(id).orElse(null);
        if (cake != null) {
            cart.addItem(new CartItem(cake, 1));
        }
        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {
        cart.removeItem(id);
        return "redirect:/cart";
    }

    @GetMapping("/clear")
    public String clearCart() {
        cart.clear();
        return "redirect:/cart";
    }
}
