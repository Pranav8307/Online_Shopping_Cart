<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Products - Admin</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <main class="container">
        <div class="page-header">
            <h1>Manage Products</h1>
            <a href="products?action=create" class="btn btn-primary">Add New Product</a>
        </div>
        
        <table class="data-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="product" items="${products}">
                    <tr class="${product.isLowStock() ? 'low-stock-row' : ''}">
                        <td>${product.id}</td>
                        <td>${product.name}</td>
                        <td>${product.category}</td>
                        <td>$${product.price}</td>
                        <td class="${product.isLowStock() ? 'low-stock' : ''}">${product.stock}</td>
                        <td>
                            <a href="products?action=edit&id=${product.id}" class="btn btn-sm btn-primary">Edit</a>
                            <a href="products?action=delete&id=${product.id}" 
                               class="btn btn-sm btn-danger" 
                               onclick="return confirm('Are you sure you want to delete this product?')">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>

