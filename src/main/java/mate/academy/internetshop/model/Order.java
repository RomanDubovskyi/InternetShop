package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private Long orderId;
    private Long ownerId;
    private List<Item> purchasedItems;

    public Order(Long orderId) {
        this.orderId = orderId;
        purchasedItems = new ArrayList<>();
    }

    public Order() {
        purchasedItems = new ArrayList<>();
    }

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
        return "Order{"
                + "orderId=" + orderId + ", ownerId=" + ownerId
                + ", purchasedItems=" + purchasedItems + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Order order = (Order) o;
        return orderId.equals(order.orderId)
                && ownerId.equals(order.ownerId)
                && purchasedItems.equals(order.purchasedItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, ownerId, purchasedItems);
    }
}
