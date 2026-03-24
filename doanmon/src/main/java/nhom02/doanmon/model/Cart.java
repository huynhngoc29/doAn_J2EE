package nhom02.doanmon.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class Cart {

    private List<CartItem> items = new ArrayList<>();

    public void addItem(CartItem newItem) {
        // If it's a completely new custom design, it will have a different ID and shouldn't merge quantity
        // However, if we add another item with NO custom image and same cake ID, we can merge.
        if (newItem.getCustomImage() == null) {
            for (CartItem item : items) {
                if (item.getCake().getId().equals(newItem.getCake().getId()) && item.getCustomImage() == null) {
                    item.setQuantity(item.getQuantity() + newItem.getQuantity());
                    return;
                }
            }
        }
        items.add(newItem);
    }

    public void removeItem(String itemId) {
        items.removeIf(item -> item.getId().equals(itemId));
    }

    public void updateQuantity(String itemId, int quantity) {
        for (CartItem item : items) {
            if (item.getId().equals(itemId)) {
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

    // getters/setters
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }
}
