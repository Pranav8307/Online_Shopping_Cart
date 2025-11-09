<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Orders - Admin</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <main class="container">
        <h1>Manage Orders</h1>
        
        <div class="filters">
            <form method="get" action="orders" class="filter-form">
                <select name="status">
                    <option value="">All Status</option>
                    <option value="PENDING" ${param.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                    <option value="CONFIRMED" ${param.status == 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                    <option value="SHIPPED" ${param.status == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                    <option value="DELIVERED" ${param.status == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                    <option value="CANCELLED" ${param.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                </select>
                <input type="date" name="startDate" value="${param.startDate}">
                <input type="date" name="endDate" value="${param.endDate}">
                <button type="submit" class="btn btn-primary">Filter</button>
                <a href="orders" class="btn btn-secondary">Clear</a>
            </form>
        </div>
        
        <table class="data-table">
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>User ID</th>
                    <th>Date</th>
                    <th>Status</th>
                    <th>Total Amount</th>
                    <th>Payment Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>#${order.orderId}</td>
                        <td>${order.userId}</td>
                        <td>${order.orderDate}</td>
                        <td>
                            <form action="orders" method="post" class="inline-form">
                                <input type="hidden" name="action" value="updateStatus">
                                <input type="hidden" name="orderId" value="${order.orderId}">
                                <select name="status" onchange="this.form.submit()">
                                    <option value="PENDING" ${order.status == 'PENDING' ? 'selected' : ''}>Pending</option>
                                    <option value="CONFIRMED" ${order.status == 'CONFIRMED' ? 'selected' : ''}>Confirmed</option>
                                    <option value="SHIPPED" ${order.status == 'SHIPPED' ? 'selected' : ''}>Shipped</option>
                                    <option value="DELIVERED" ${order.status == 'DELIVERED' ? 'selected' : ''}>Delivered</option>
                                    <option value="CANCELLED" ${order.status == 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                                </select>
                            </form>
                        </td>
                        <td>$${order.totalAmount}</td>
                        <td>${order.paymentStatus}</td>
                        <td>
                            <a href="orders?orderId=${order.orderId}" class="btn btn-sm btn-primary">View Details</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </main>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>

