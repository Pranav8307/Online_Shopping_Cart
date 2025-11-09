package com.shoppingcart.servlet.admin;

import com.shoppingcart.dao.ProductDAO;
import com.shoppingcart.model.Product;
import com.shoppingcart.service.EmailService;
import com.shoppingcart.util.AuthHelper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireAdmin(request, response)) {
            return;
        }
        
        ProductDAO productDAO = ProductDAO.getInstance();
        String action = request.getParameter("action");
        
        if ("edit".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = productDAO.getProductById(id);
            request.setAttribute("product", product);
            request.setAttribute("categories", productDAO.getAllCategories());
            request.getRequestDispatcher("/admin/product-form.jsp").forward(request, response);
        } else if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            productDAO.deleteProduct(id);
            response.sendRedirect(request.getContextPath() + "/admin/products");
        } else {
            request.setAttribute("products", productDAO.getAllProducts());
            request.setAttribute("lowStockProducts", productDAO.getLowStockProducts());
            request.getRequestDispatcher("/admin/products.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireAdmin(request, response)) {
            return;
        }
        
        ProductDAO productDAO = ProductDAO.getInstance();
        String action = request.getParameter("action");
        
        if ("create".equals(action) || "update".equals(action)) {
            Product product = new Product();
            
            if ("update".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                product.setId(id);
            }
            
            product.setName(request.getParameter("name"));
            product.setDescription(request.getParameter("description"));
            product.setPrice(new BigDecimal(request.getParameter("price")));
            product.setCategory(request.getParameter("category"));
            product.setStock(Integer.parseInt(request.getParameter("stock")));
            product.setImageUrl(request.getParameter("imageUrl"));
            
            if ("create".equals(action)) {
                productDAO.createProduct(product);
            } else {
                productDAO.updateProduct(product);
            }
            
            // Check for low stock and send alert
            if (product.isLowStock()) {
                EmailService.sendLowStockAlert(product.getName(), product.getStock());
            }
        }
        
        response.sendRedirect(request.getContextPath() + "/admin/products");
    }
}

