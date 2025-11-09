package com.shoppingcart.dao;

import com.shoppingcart.model.Order;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrderDAO {
    private static OrderDAO instance;
    private Map<Integer, Order> orders;
    private AtomicInteger idGenerator;
    
    private OrderDAO() {
        orders = new ConcurrentHashMap<>();
        idGenerator = new AtomicInteger(1);
    }
    
    public static synchronized OrderDAO getInstance() {
        if (instance == null) {
            instance = new OrderDAO();
        }
        return instance;
    }
    
    public Order createOrder(Order order) {
        order.setOrderId(idGenerator.getAndIncrement());
        orders.put(order.getOrderId(), order);
        return order;
    }
    
    public Order getOrderById(int orderId) {
        return orders.get(orderId);
    }
    
    public List<Order> getOrdersByUserId(int userId) {
        return orders.values().stream()
                .filter(o -> o.getUserId() == userId)
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .collect(Collectors.toList());
    }
    
    public List<Order> getAllOrders() {
        return orders.values().stream()
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .collect(Collectors.toList());
    }
    
    public List<Order> getOrdersByStatus(String status) {
        return orders.values().stream()
                .filter(o -> o.getStatus().equalsIgnoreCase(status))
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .collect(Collectors.toList());
    }
    
    public List<Order> getOrdersByDateRange(Date startDate, Date endDate) {
        return orders.values().stream()
                .filter(o -> !o.getOrderDate().before(startDate) && !o.getOrderDate().after(endDate))
                .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
                .collect(Collectors.toList());
    }
    
    public boolean updateOrder(Order order) {
        if (orders.containsKey(order.getOrderId())) {
            orders.put(order.getOrderId(), order);
            return true;
        }
        return false;
    }
    
    public boolean deleteOrder(int orderId) {
        return orders.remove(orderId) != null;
    }
}

