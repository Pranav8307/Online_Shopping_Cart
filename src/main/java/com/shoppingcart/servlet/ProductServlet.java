package com.shoppingcart.servlet;

import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.model.Product;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDAO productDAO = ProductDAO.getInstance();
        String action = request.getParameter("action");
        String search = request.getParameter("search");
        String category = request.getParameter("category");
        String minPrice = request.getParameter("minPrice");
        String maxPrice = request.getParameter("maxPrice");
        
        List<Product> products;
        
        if (search != null && !search.isEmpty()) {
            products = productDAO.searchProducts(search);
        } else if (category != null && !category.isEmpty()) {
            products = productDAO.filterByCategory(category);
        } else if (minPrice != null && maxPrice != null && !minPrice.isEmpty() && !maxPrice.isEmpty()) {
            products = productDAO.filterByPriceRange(
                new BigDecimal(minPrice), new BigDecimal(maxPrice));
        } else {
            products = productDAO.getAllProducts();
        }
        
        request.setAttribute("products", products);
        request.setAttribute("categories", productDAO.getAllCategories());
        request.getRequestDispatcher("/products.jsp").forward(request, response);
    }
}

