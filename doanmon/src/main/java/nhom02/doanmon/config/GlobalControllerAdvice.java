package nhom02.doanmon.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import nhom02.doanmon.model.Cart;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private Cart cart;

    @ModelAttribute("cart")
    public Cart populateCart() {
        return cart;
    }

    @ModelAttribute("cartSize")
    public int populateCartSize() {
        return cart.getItems().size();
    }
}
