package mate.academy.internetshop.model;

import java.util.List;

public class Order {
    private Long orderId;
    private List<Item> purchasedItems;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<Item> getItems() {
        return purchasedItems;
    }

    public void setItems(List<Item> items) {
        this.purchasedItems = items;
    }
}
