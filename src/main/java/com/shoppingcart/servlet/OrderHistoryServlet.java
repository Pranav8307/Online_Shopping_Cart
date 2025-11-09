package com.shoppingcart.servlet;

import com.shoppingcart.dao.OrderDAO;
import com.shoppingcart.model.Order;
import com.shoppingcart.model.User;
import com.shoppingcart.util.AuthHelper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/orders")
public class OrderHistoryServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireLogin(request, response)) {
            return;
        }
        
        HttpSession session = request.getSession();
        User user = AuthHelper.getCurrentUser(request);
        
        OrderDAO orderDAO = OrderDAO.getInstance();
        List<Order> orders = orderDAO.getOrdersByUserId(user.getId());
        
        request.setAttribute("orders", orders);
        request.getRequestDispatcher("/orders.jsp").forward(request, response);
    }
}

