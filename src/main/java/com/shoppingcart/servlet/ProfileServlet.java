package com.shoppingcart.servlet;

import com.shoppingcart.dao.UserDAO;
import com.shoppingcart.model.User;
import com.shoppingcart.util.AuthHelper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireLogin(request, response)) {
            return;
        }
        
        HttpSession session = request.getSession();
        User user = AuthHelper.getCurrentUser(request);
        
        // Get fresh user data
        User freshUser = UserDAO.getInstance().getUserById(user.getId());
        request.setAttribute("user", freshUser);
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireLogin(request, response)) {
            return;
        }
        
        HttpSession session = request.getSession();
        User sessionUser = AuthHelper.getCurrentUser(request);
        
        UserDAO userDAO = UserDAO.getInstance();
        User user = userDAO.getUserById(sessionUser.getId());
        
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setAddress(request.getParameter("address"));
        user.setCity(request.getParameter("city"));
        user.setState(request.getParameter("state"));
        user.setZipCode(request.getParameter("zipCode"));
        
        String newPassword = request.getParameter("newPassword");
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            user.setPassword(newPassword);
        }
        
        userDAO.updateUser(user);
        session.setAttribute("user", user);
        
        response.sendRedirect(request.getContextPath() + "/profile?success=Profile updated successfully");
    }
}

