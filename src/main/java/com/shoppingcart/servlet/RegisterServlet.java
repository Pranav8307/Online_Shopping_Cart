package com.shoppingcart.servlet;

import com.shoppingcart.dao.UserDAO;
import com.shoppingcart.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String zipCode = request.getParameter("zipCode");
        
        UserDAO userDAO = UserDAO.getInstance();
        
        // Check if username already exists
        if (userDAO.getUserByUsername(username) != null) {
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=Username already exists");
            return;
        }
        
        User newUser = new User(username, email, password, "CUSTOMER");
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setPhone(phone);
        newUser.setAddress(address);
        newUser.setCity(city);
        newUser.setState(state);
        newUser.setZipCode(zipCode);
        
        User createdUser = userDAO.createUser(newUser);
        
        if (createdUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", createdUser);
            response.sendRedirect(request.getContextPath() + "/products");
        } else {
            response.sendRedirect(request.getContextPath() + "/register.jsp?error=Registration failed");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
}

