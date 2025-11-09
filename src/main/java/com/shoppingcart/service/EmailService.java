package com.shoppingcart.service;

import com.shoppingcart.model.Order;
import com.shoppingcart.model.User;
import com.shoppingcart.dao.UserDAO;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailService {
    private static final String SMTP_HOST = System.getenv("SMTP_HOST") != null ? System.getenv("SMTP_HOST") : "smtp.gmail.com";
    private static final String SMTP_PORT = System.getenv("SMTP_PORT") != null ? System.getenv("SMTP_PORT") : "587";
    private static final String FROM_EMAIL = System.getenv("EMAIL_FROM") != null ? System.getenv("EMAIL_FROM") : "noreply@example.com";
    private static final String FROM_PASSWORD = System.getenv("EMAIL_PASSWORD") != null ? System.getenv("EMAIL_PASSWORD") : "";
    
    public static boolean sendOrderConfirmation(Order order) {
        User user = UserDAO.getInstance().getUserById(order.getUserId());
        if (user == null || user.getEmail() == null) {
            return false;
        }
        
        String subject = "Order Confirmation - Order #" + order.getOrderId();
        String body = buildOrderConfirmationEmail(order, user);
        
        return sendEmail(user.getEmail(), subject, body);
    }
    
    public static boolean sendLowStockAlert(String productName, int stock) {
        String adminEmail = "admin@shop.com"; // Configure admin email
        String subject = "Low Stock Alert - " + productName;
        String body = "Product: " + productName + " has low stock (" + stock + " units remaining).\n" +
                     "Please restock soon.";
        
        return sendEmail(adminEmail, subject, body);
    }
    
    private static String buildOrderConfirmationEmail(Order order, User user) {
        StringBuilder email = new StringBuilder();
        email.append("Dear ").append(user.getFirstName()).append(" ").append(user.getLastName()).append(",\n\n");
        email.append("Thank you for your order!\n\n");
        email.append("Order Details:\n");
        email.append("Order ID: #").append(order.getOrderId()).append("\n");
        email.append("Order Date: ").append(order.getOrderDate()).append("\n");
        email.append("Status: ").append(order.getStatus()).append("\n");
        email.append("Total Amount: $").append(order.getTotalAmount()).append("\n\n");
        email.append("Items:\n");
        for (com.shoppingcart.model.OrderItem item : order.getItems()) {
            email.append("- ").append(item.getProductName())
                 .append(" (Qty: ").append(item.getQuantity())
                 .append(", Price: $").append(item.getPrice())
                 .append(")\n");
        }
        email.append("\nShipping Address:\n").append(order.getShippingAddress()).append("\n\n");
        email.append("We will notify you once your order is shipped.\n\n");
        email.append("Best regards,\nOnline Shopping Cart Team");
        return email.toString();
    }
    
    private static boolean sendEmail(String toEmail, String subject, String body) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, FROM_PASSWORD);
                }
            });
            
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);
            
            Transport.send(message);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            // In production, you might want to log this properly
            return false;
        }
    }
}

