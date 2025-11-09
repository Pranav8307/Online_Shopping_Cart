package com.shoppingcart.servlet.admin;

import com.shoppingcart.dao.OrderDAO;
import com.shoppingcart.model.Order;
import com.shoppingcart.util.AuthHelper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireAdmin(request, response)) {
            return;
        }
        
        OrderDAO orderDAO = OrderDAO.getInstance();
        String action = request.getParameter("action");
        String status = request.getParameter("status");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        
        List<Order> orders;
        
        if (status != null && !status.isEmpty()) {
            orders = orderDAO.getOrdersByStatus(status);
        } else if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);
                orders = orderDAO.getOrdersByDateRange(start, end);
            } catch (ParseException e) {
                orders = orderDAO.getAllOrders();
            }
        } else {
            orders = orderDAO.getAllOrders();
        }
        
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/admin/orders.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireAdmin(request, response)) {
            return;
        }
        
        OrderDAO orderDAO = OrderDAO.getInstance();
        String action = request.getParameter("action");
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        
        Order order = orderDAO.getOrderById(orderId);
        if (order != null) {
            if ("updateStatus".equals(action)) {
                String newStatus = request.getParameter("status");
                order.setStatus(newStatus);
                orderDAO.updateOrder(order);
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/orders");
    }
}

