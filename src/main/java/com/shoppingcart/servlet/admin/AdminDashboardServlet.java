package com.shoppingcart.servlet.admin;

import com.shoppingcart.dao.OrderDAO;
import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.dao.UserDAO;
import com.shoppingcart.model.Order;
import com.shoppingcart.util.AuthHelper;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class AdminDashboardServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireAdmin(request, response)) {
            return;
        }
        
        OrderDAO orderDAO = OrderDAO.getInstance();
        ProductDAO productDAO = ProductDAO.getInstance();
        UserDAO userDAO = UserDAO.getInstance();
        
        List<Order> allOrders = orderDAO.getAllOrders();
        List<Order> recentOrders = allOrders.stream()
                .limit(10)
                .collect(java.util.stream.Collectors.toList());
        
        BigDecimal totalRevenue = allOrders.stream()
                .filter(o -> "COMPLETED".equalsIgnoreCase(o.getPaymentStatus()))
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalOrders = allOrders.size();
        int totalProducts = productDAO.getAllProducts().size();
        int totalCustomers = userDAO.getAllCustomers().size();
        int lowStockCount = productDAO.getLowStockProducts().size();
        
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalProducts", totalProducts);
        request.setAttribute("totalCustomers", totalCustomers);
        request.setAttribute("lowStockCount", lowStockCount);
        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("lowStockProducts", productDAO.getLowStockProducts());
        
        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}

