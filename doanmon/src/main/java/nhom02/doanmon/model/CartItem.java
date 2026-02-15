package nhom02.doanmon.model;

import nhom02.doanmon.entity.Cake;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Cake cake;
    private int quantity;

    public double getTotalPrice() {
        return cake.getPrice() * quantity;
    }
}
