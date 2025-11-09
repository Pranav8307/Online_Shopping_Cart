<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page import="com.shoppingcart.dao.OrderDAO" %>
<%@ page import="com.shoppingcart.model.Order" %>
<%
    String orderIdParam = request.getParameter("orderId");
    Order order = null;
    if (orderIdParam != null) {
        order = OrderDAO.getInstance().getOrderById(Integer.parseInt(orderIdParam));
    }
    request.setAttribute("order", order);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation - Online Shopping Cart</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="container">
        <c:choose>
            <c:when test="${order != null}">
                <div class="confirmation-container">
                    <div class="confirmation-icon">✓</div>
                    <h1>Order Confirmed!</h1>
                    <p class="order-id">Order ID: #${order.orderId}</p>
                    
                    <div class="order-details">
                        <h2>Order Details</h2>
                        <p><strong>Order Date:</strong> ${order.orderDate}</p>
                        <p><strong>Status:</strong> ${order.status}</p>
                        <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
                        <p><strong>Total Amount:</strong> $${order.totalAmount}</p>
                        
                        <h3>Items Ordered</h3>
                        <table class="order-items-table">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Quantity</th>
                                    <th>Price</th>
                                    <th>Subtotal</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${order.items}">
                                    <tr>
                                        <td>${item.productName}</td>
                                        <td>${item.quantity}</td>
                                        <td>$${item.price}</td>
                                        <td>$${item.subtotal}</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        
                        <p><strong>Shipping Address:</strong> ${order.shippingAddress}</p>
                    </div>
                    
                    <div class="confirmation-actions">
                        <a href="orders" class="btn btn-primary">View All Orders</a>
                        <a href="products" class="btn btn-secondary">Continue Shopping</a>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="empty-state">
                    <p>Order not found.</p>
                    <a href="orders" class="btn btn-primary">View Orders</a>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
    
    <jsp:include page="footer.jsp" />
</body>
</html>

