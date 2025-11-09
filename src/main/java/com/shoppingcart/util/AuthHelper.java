package com.shoppingcart.util;

import com.shoppingcart.model.User;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthHelper {
    
    public static User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            return (User) session.getAttribute("user");
        }
        return null;
    }
    
    public static boolean requireLogin(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?error=Please login first");
            return false;
        }
        return true;
    }
    
    public static boolean requireAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        if (!requireLogin(request, response)) {
            return false;
        }
        User user = getCurrentUser(request);
        if (user == null || !user.isAdmin()) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied. Admin privileges required.");
            return false;
        }
        return true;
    }
}

