package mate.academy.internetshop.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Bucket {
    private List<Item> items;
    private Long bucketId;
    private Long ownerId;

    public Bucket() {
        items = new ArrayList<>();
    }

    public Bucket(Long ownerId) {
        this.ownerId = ownerId;
        items = new ArrayList<>();
    }

    public Long getOwnerID() {
        return ownerId;
    }

    public void setOwnerId(Long id) {
        this.ownerId = id;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Long getBucketId() {
        return bucketId;
    }

    public void setBucketId(Long bucketId) {
        this.bucketId = bucketId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bucket bucket = (Bucket) o;
        return bucketId.equals(bucket.bucketId)
                && ownerId.equals(bucket.ownerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bucketId, ownerId);
    }
}
