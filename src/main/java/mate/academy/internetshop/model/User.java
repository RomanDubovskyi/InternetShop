package mate.academy.internetshop.model;

import java.util.List;

public class User {
    private Long userId;
    private Bucket bucket;
    private List<Order> ordersHistory;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Bucket getBucket() {
        return bucket;
    }

    public void setBucket(Bucket bucket) {
        this.bucket = bucket;
    }

    public List<Order> getOrdersHistory() {
        return ordersHistory;
    }

    public void setOrdersHistory(List<Order> ordersHistory) {
        this.ordersHistory = ordersHistory;
    }
}
