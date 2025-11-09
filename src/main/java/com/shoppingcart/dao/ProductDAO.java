package com.shoppingcart.dao;

import com.shoppingcart.model.Product;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ProductDAO {
    private static ProductDAO instance;
    private Map<Integer, Product> products;
    private AtomicInteger idGenerator;
    
    private ProductDAO() {
        products = new ConcurrentHashMap<>();
        idGenerator = new AtomicInteger(1);
        initializeSampleProducts();
    }
    
    public static synchronized ProductDAO getInstance() {
        if (instance == null) {
            instance = new ProductDAO();
        }
        return instance;
    }
    
    private void initializeSampleProducts() {
        addProduct(new Product("Laptop", "High-performance laptop with 16GB RAM", new BigDecimal("999.99"), "Electronics", 50));
        addProduct(new Product("Smartphone", "Latest smartphone with 128GB storage", new BigDecimal("699.99"), "Electronics", 100));
        addProduct(new Product("Headphones", "Wireless noise-cancelling headphones", new BigDecimal("199.99"), "Electronics", 75));
        addProduct(new Product("T-Shirt", "Cotton t-shirt in various colors", new BigDecimal("24.99"), "Clothing", 200));
        addProduct(new Product("Jeans", "Classic fit denim jeans", new BigDecimal("59.99"), "Clothing", 150));
        addProduct(new Product("Running Shoes", "Comfortable running shoes", new BigDecimal("89.99"), "Footwear", 80));
        addProduct(new Product("Coffee Maker", "Programmable coffee maker", new BigDecimal("79.99"), "Appliances", 60));
        addProduct(new Product("Book: Java Programming", "Complete guide to Java programming", new BigDecimal("49.99"), "Books", 120));
    }
    
    private void addProduct(Product product) {
        product.setId(idGenerator.getAndIncrement());
        products.put(product.getId(), product);
    }
    
    public Product createProduct(Product product) {
        product.setId(idGenerator.getAndIncrement());
        products.put(product.getId(), product);
        return product;
    }
    
    public Product getProductById(int id) {
        return products.get(id);
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }
    
    public List<Product> searchProducts(String searchTerm) {
        String term = searchTerm.toLowerCase();
        return products.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(term) ||
                            p.getDescription().toLowerCase().contains(term) ||
                            p.getCategory().toLowerCase().contains(term))
                .collect(Collectors.toList());
    }
    
    public List<Product> filterByCategory(String category) {
        return products.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
    
    public List<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return products.values().stream()
                .filter(p -> p.getPrice().compareTo(minPrice) >= 0 && 
                            p.getPrice().compareTo(maxPrice) <= 0)
                .collect(Collectors.toList());
    }
    
    public List<String> getAllCategories() {
        return products.values().stream()
                .map(Product::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
    
    public List<Product> getLowStockProducts() {
        return products.values().stream()
                .filter(Product::isLowStock)
                .collect(Collectors.toList());
    }
    
    public boolean updateProduct(Product product) {
        if (products.containsKey(product.getId())) {
            products.put(product.getId(), product);
            return true;
        }
        return false;
    }
    
    public boolean deleteProduct(int id) {
        return products.remove(id) != null;
    }
    
    public boolean updateStock(int productId, int quantity) {
        Product product = products.get(productId);
        if (product != null) {
            product.setStock(product.getStock() + quantity);
            return true;
        }
        return false;
    }
    
    public boolean reduceStock(int productId, int quantity) {
        Product product = products.get(productId);
        if (product != null && product.getStock() >= quantity) {
            product.setStock(product.getStock() - quantity);
            return true;
        }
        return false;
    }
}

