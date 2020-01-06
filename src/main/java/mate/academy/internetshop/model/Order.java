package mate.academy.internetshop.model;

import java.util.List;

public class Order {
    private Long orderId;
    private Long ownerId;
    private List<Item> purchasedItems;

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

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

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", ownerId=" + ownerId +
                ", purchasedItems=" + purchasedItems +
                '}';
    }
}
