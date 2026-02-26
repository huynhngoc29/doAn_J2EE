package nhom02.doanmon.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
@Data
public class Cart {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem newItem) {
        for (CartItem item : items) {
            if (item.getCake().getId().equals(newItem.getCake().getId())) {
                item.setQuantity(item.getQuantity() + newItem.getQuantity());
                return;
            }
        }
        items.add(newItem);
    }

    public void removeItem(Long cakeId) {
        items.removeIf(item -> item.getCake().getId().equals(cakeId));
    }

    public void updateQuantity(Long cakeId, int quantity) {
        for (CartItem item : items) {
            if (item.getCake().getId().equals(cakeId)) {
                if (quantity <= 0) {
                    items.remove(item);
                } else {
                    item.setQuantity(quantity);
                }
                return;
            }
        }
    }

    public void clear() {
        items.clear();
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(CartItem::getTotalPrice).sum();
    }
}
