package com.shoppingcart.servlet;

import com.shoppingcart.dao.OrderDAO;
import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.model.*;
import com.shoppingcart.service.EmailService;
import com.shoppingcart.util.AuthHelper;
import com.shoppingcart.util.XMLOrderManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireLogin(request, response)) {
            return;
        }
        
        HttpSession session = request.getSession();
        User user = AuthHelper.getCurrentUser(request);
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        
        BigDecimal total = calculateTotal(cart);
        request.setAttribute("cart", cart);
        request.setAttribute("total", total);
        request.setAttribute("user", user);
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireLogin(request, response)) {
            return;
        }
        
        HttpSession session = request.getSession();
        User user = AuthHelper.getCurrentUser(request);
        @SuppressWarnings("unchecked")
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
        
        if (cart == null || cart.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }
        
        String paymentMethod = request.getParameter("paymentMethod");
        String shippingAddress = request.getParameter("shippingAddress");
        
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            shippingAddress = user.getAddress() + ", " + user.getCity() + ", " + 
                            user.getState() + " " + user.getZipCode();
        }
        
        ProductDAO productDAO = ProductDAO.getInstance();
        
        // Validate stock and create order
        Order order = new Order();
        order.setUserId(user.getId());
        order.setShippingAddress(shippingAddress);
        order.setPaymentMethod(paymentMethod);
        order.setStatus("CONFIRMED");
        order.setPaymentStatus("COMPLETED");
        
        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        
        for (CartItem cartItem : cart) {
            Product product = cartItem.getProduct();
            if (product.getStock() < cartItem.getQuantity()) {
                request.setAttribute("error", "Insufficient stock for " + product.getName());
                doGet(request, response);
                return;
            }
            
            OrderItem orderItem = new OrderItem(
                product.getId(),
                product.getName(),
                cartItem.getQuantity(),
                product.getPrice()
            );
            orderItems.add(orderItem);
            total = total.add(orderItem.getSubtotal());
            
            // Reduce stock
            productDAO.reduceStock(product.getId(), cartItem.getQuantity());
        }
        
        order.setItems(orderItems);
        order.setTotalAmount(total);
        
        // Save order
        OrderDAO orderDAO = OrderDAO.getInstance();
        orderDAO.createOrder(order);
        
        // Save to XML
        XMLOrderManager.saveOrderToXML(order);
        
        // Send email confirmation
        EmailService.sendOrderConfirmation(order);
        
        // Clear cart
        session.removeAttribute("cart");
        
        // Redirect to order confirmation
        response.sendRedirect(request.getContextPath() + "/order-confirmation.jsp?orderId=" + order.getOrderId());
    }
    
    private BigDecimal calculateTotal(List<CartItem> cart) {
        return cart.stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

