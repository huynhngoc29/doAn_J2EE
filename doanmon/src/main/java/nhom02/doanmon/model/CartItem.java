package nhom02.doanmon.model;

import nhom02.doanmon.entity.Cake;
// lombok removed

public class CartItem {

    private Cake cake;
    private int quantity;

    public CartItem() {
    }

    public CartItem(Cake cake, int quantity) {
        this.cake = cake;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return cake.getPrice() * quantity;
    }

    // getters/setters
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
}
