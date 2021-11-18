package io.leego.commons.standard;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Leego Yih
 */
public class Sort {
    private List<Order> orders;

    public Sort() {
        this.orders = new ArrayList<>();
    }

    public Sort(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
