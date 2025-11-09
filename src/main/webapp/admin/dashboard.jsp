<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Online Shopping Cart</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <main class="container">
        <h1>Admin Dashboard</h1>
        
        <div class="dashboard-stats">
            <div class="stat-card">
                <h3>Total Revenue</h3>
                <p class="stat-value">$${totalRevenue}</p>
            </div>
            <div class="stat-card">
                <h3>Total Orders</h3>
                <p class="stat-value">${totalOrders}</p>
            </div>
            <div class="stat-card">
                <h3>Total Products</h3>
                <p class="stat-value">${totalProducts}</p>
            </div>
            <div class="stat-card">
                <h3>Total Customers</h3>
                <p class="stat-value">${totalCustomers}</p>
            </div>
            <div class="stat-card alert">
                <h3>Low Stock Items</h3>
                <p class="stat-value">${lowStockCount}</p>
            </div>
        </div>
        
        <div class="dashboard-sections">
            <div class="dashboard-section">
                <h2>Quick Actions</h2>
                <div class="action-buttons">
                    <a href="products" class="btn btn-primary">Manage Products</a>
                    <a href="orders" class="btn btn-primary">View Orders</a>
                    <a href="reports" class="btn btn-primary">View Reports</a>
                </div>
            </div>
            
            <div class="dashboard-section">
                <h2>Low Stock Alerts</h2>
                <c:choose>
                    <c:when test="${not empty lowStockProducts}">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Category</th>
                                    <th>Stock</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="product" items="${lowStockProducts}">
                                    <tr>
                                        <td>${product.name}</td>
                                        <td>${product.category}</td>
                                        <td class="low-stock">${product.stock}</td>
                                        <td><a href="products?action=edit&id=${product.id}" class="btn btn-sm btn-primary">Update</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p>No low stock items.</p>
                    </c:otherwise>
                </c:choose>
            </div>
            
            <div class="dashboard-section">
                <h2>Recent Orders</h2>
                <c:choose>
                    <c:when test="${not empty recentOrders}">
                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>Order ID</th>
                                    <th>Date</th>
                                    <th>Status</th>
                                    <th>Amount</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="order" items="${recentOrders}">
                                    <tr>
                                        <td>#${order.orderId}</td>
                                        <td>${order.orderDate}</td>
                                        <td>${order.status}</td>
                                        <td>$${order.totalAmount}</td>
                                        <td><a href="orders?orderId=${order.orderId}" class="btn btn-sm btn-primary">View</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:when>
                    <c:otherwise>
                        <p>No recent orders.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </main>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>

