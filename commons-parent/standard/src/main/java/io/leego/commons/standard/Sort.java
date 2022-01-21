package io.leego.commons.standard;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Leego Yih
 */
public class Sort implements Serializable {
    @Serial
    private static final long serialVersionUID = -7445270943868879346L;
    private static final Sort UNSORTED = Sort.by(new Order[0]);
    private static final Direction DEFAULT_DIRECTION = Direction.ASC;

    private List<Order> orders;

    protected Sort() {
    }

    protected Sort(List<Order> orders) {
        this.orders = orders;
    }

    protected Sort(Direction direction, List<String> properties) {

        if (properties == null || properties.isEmpty()) {
            throw new IllegalArgumentException("You have to provide at least one property to sort by!");
        }

        this.orders = properties.stream()
                .map(it -> new Order(direction, it))
                .collect(Collectors.toList());
    }

    public static Sort by(String... properties) {
        return properties == null || properties.length == 0
                ? Sort.unsorted()
                : new Sort(DEFAULT_DIRECTION, Arrays.asList(properties));
    }

    public static Sort by(List<Order> orders) {
        return orders == null || orders.isEmpty() ? Sort.unsorted() : new Sort(orders);
    }

    public static Sort by(Order... orders) {
        return orders == null || orders.length == 0 ? Sort.unsorted() : new Sort(Arrays.asList(orders));
    }

    public static Sort by(Direction direction, String... properties) {
        if (properties == null) {
            return Sort.unsorted();
        }
        return Sort.by(Arrays.stream(properties)
                .map(it -> new Order(direction, it))
                .collect(Collectors.toList()));
    }

    public static Sort unsorted() {
        return UNSORTED;
    }

    public Sort descending() {
        return withDirection(Direction.DESC);
    }

    public Sort ascending() {
        return withDirection(Direction.ASC);
    }

    public boolean isSorted() {
        return !isEmpty();
    }

    public boolean isUnsorted() {
        return !isSorted();
    }

    public boolean isEmpty() {
        return orders.isEmpty();
    }

    public Sort and(Sort sort) {
        if (sort == null) {
            return Sort.by(this.orders);
        }
        ArrayList<Order> these = new ArrayList<>(this.orders);
        these.addAll(sort.orders);
        return Sort.by(these);
    }

    private Sort withDirection(Direction direction) {
        return Sort.by(this.orders.stream().map(it -> new Order(direction, it.getProperty())).collect(Collectors.toList()));
    }

    public Order getOrderFor(String property) {
        for (Order order : this.orders) {
            if (order.getProperty().equals(property)) {
                return order;
            }
        }
        return null;
    }

    public Iterator<Order> iterator() {
        return this.orders.iterator();
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Sort that)) {
            return false;
        }
        return orders.equals(that.orders);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + orders.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return isEmpty() ? "UNSORTED" : orders.toString();
    }
}
