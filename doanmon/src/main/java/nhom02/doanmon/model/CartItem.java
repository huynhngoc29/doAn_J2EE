package nhom02.doanmon.model;

import java.util.UUID;
import nhom02.doanmon.entity.Cake;

public class CartItem {

    private String id;
    private Cake cake;
    private int quantity;
    private String customImage;

    public CartItem() {
        this.id = UUID.randomUUID().toString();
    }

    public CartItem(Cake cake, int quantity) {
        this.id = UUID.randomUUID().toString();
        this.cake = cake;
        this.quantity = quantity;
    }

    public CartItem(Cake cake, int quantity, String customImage) {
        this.id = UUID.randomUUID().toString();
        this.cake = cake;
        this.quantity = quantity;
        this.customImage = customImage;
    }

    public double getTotalPrice() {
        return cake.getPrice() * quantity;
    }

    // getters/setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cake getCake() {
        return cake;
    }

    public void setCake(Cake cake) {
        this.cake = cake;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getCustomImage() {
        return customImage;
    }

    public void setCustomImage(String customImage) {
        this.customImage = customImage;
    }
}
