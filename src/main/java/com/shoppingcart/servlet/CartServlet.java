package com.shoppingcart.servlet;

import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.model.CartItem;
import com.shoppingcart.model.Product;
import com.shoppingcart.model.User;
import com.shoppingcart.util.AuthHelper;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CartServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireLogin(request, response)) {
            return;
        }
        
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        
        BigDecimal total = calculateTotal(cart);
        request.setAttribute("cart", cart);
        request.setAttribute("total", total);
        request.getRequestDispatcher("/cart.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireLogin(request, response)) {
            return;
        }
        
        String path = request.getPathInfo();
        HttpSession session = request.getSession();
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        
        if (path == null || path.equals("/add")) {
            addToCart(request, cart);
        } else if (path.equals("/update")) {
            updateCartItem(request, cart);
        } else if (path.equals("/remove")) {
            removeFromCart(request, cart);
        }
        
        session.setAttribute("cart", cart);
        response.sendRedirect(request.getContextPath() + "/cart");
    }
    
    private void addToCart(HttpServletRequest request, List<CartItem> cart) {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        ProductDAO productDAO = ProductDAO.getInstance();
        Product product = productDAO.getProductById(productId);
        
        if (product != null && product.isInStock() && product.getStock() >= quantity) {
            CartItem existingItem = findCartItem(cart, productId);
            if (existingItem != null) {
                int newQuantity = existingItem.getQuantity() + quantity;
                if (newQuantity <= product.getStock()) {
                    existingItem.setQuantity(newQuantity);
                }
            } else {
                cart.add(new CartItem(product, quantity));
            }
        }
    }
    
    private void updateCartItem(HttpServletRequest request, List<CartItem> cart) {
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        
        CartItem item = findCartItem(cart, productId);
        if (item != null) {
            Product product = item.getProduct();
            if (quantity <= product.getStock()) {
                item.setQuantity(quantity);
            }
        }
    }
    
    private void removeFromCart(HttpServletRequest request, List<CartItem> cart) {
        int productId = Integer.parseInt(request.getParameter("productId"));
        cart.removeIf(item -> item.getProduct().getId() == productId);
    }
    
    private CartItem findCartItem(List<CartItem> cart, int productId) {
        return cart.stream()
                .filter(item -> item.getProduct().getId() == productId)
                .findFirst()
                .orElse(null);
    }
    
    private BigDecimal calculateTotal(List<CartItem> cart) {
        return cart.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

