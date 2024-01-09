package ru.yandex.practicum.order;

import java.util.List;

public class UserOrders {
    public boolean success;
    public List<OrderFromUser> orders;
    public int total;
    public int totalToday;

    public UserOrders() {
    }

    public UserOrders(boolean success, List<OrderFromUser> orders, int total, int totalToday) {
        this.success = success;
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<OrderFromUser> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderFromUser> orders) {
        this.orders = orders;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(int totalToday) {
        this.totalToday = totalToday;
    }
}
