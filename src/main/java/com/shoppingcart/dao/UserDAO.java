package com.shoppingcart.dao;

import com.shoppingcart.model.User;
import com.shoppingcart.util.PasswordUtil;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserDAO {
    private static UserDAO instance;
    private Map<Integer, User> users;
    private Map<String, User> usersByUsername;
    private AtomicInteger idGenerator;
    
    private UserDAO() {
        users = new ConcurrentHashMap<>();
        usersByUsername = new ConcurrentHashMap<>();
        idGenerator = new AtomicInteger(1);
        initializeDefaultUsers();
    }
    
    public static synchronized UserDAO getInstance() {
        if (instance == null) {
            instance = new UserDAO();
        }
        return instance;
    }
    
    private void initializeDefaultUsers() {
        // Default admin user
        User admin = new User("admin", "admin@shop.com", PasswordUtil.hashPassword("admin123"), "ADMIN");
        admin.setId(idGenerator.getAndIncrement());
        admin.setFirstName("Admin");
        admin.setLastName("User");
        users.put(admin.getId(), admin);
        usersByUsername.put(admin.getUsername(), admin);
        
        // Default customer user
        User customer = new User("customer", "customer@shop.com", PasswordUtil.hashPassword("customer123"), "CUSTOMER");
        customer.setId(idGenerator.getAndIncrement());
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setAddress("123 Main St");
        customer.setCity("New York");
        customer.setState("NY");
        customer.setZipCode("10001");
        customer.setPhone("123-456-7890");
        users.put(customer.getId(), customer);
        usersByUsername.put(customer.getUsername(), customer);
    }
    
    public User authenticate(String username, String password) {
        User user = usersByUsername.get(username);
        if (user != null && PasswordUtil.checkPassword(password, user.getPassword())) {
            return user;
        }
        return null;
    }
    
    public User createUser(User user) {
        if (usersByUsername.containsKey(user.getUsername())) {
            return null; // Username already exists
        }
        user.setId(idGenerator.getAndIncrement());
        user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
        users.put(user.getId(), user);
        usersByUsername.put(user.getUsername(), user);
        return user;
    }
    
    public User getUserById(int id) {
        return users.get(id);
    }
    
    public User getUserByUsername(String username) {
        return usersByUsername.get(username);
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public List<User> getAllCustomers() {
        List<User> customers = new ArrayList<>();
        for (User user : users.values()) {
            if (user.isCustomer()) {
                customers.add(user);
            }
        }
        return customers;
    }
    
    public boolean updateUser(User user) {
        if (users.containsKey(user.getId())) {
            User existing = users.get(user.getId());
            // Don't update password if it's already hashed
            if (user.getPassword() != null && !user.getPassword().startsWith("$2a$")) {
                user.setPassword(PasswordUtil.hashPassword(user.getPassword()));
            } else if (user.getPassword() == null) {
                user.setPassword(existing.getPassword());
            }
            users.put(user.getId(), user);
            usersByUsername.put(user.getUsername(), user);
            return true;
        }
        return false;
    }
    
    public boolean deleteUser(int id) {
        User user = users.remove(id);
        if (user != null) {
            usersByUsername.remove(user.getUsername());
            return true;
        }
        return false;
    }
}

