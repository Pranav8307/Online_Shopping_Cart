<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders - Online Shopping Cart</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="container">
        <h1>My Orders</h1>
        
        <c:choose>
            <c:when test="${empty orders}">
                <div class="empty-state">
                    <p>You haven't placed any orders yet. <a href="products">Start shopping!</a></p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="orders-list">
                    <c:forEach var="order" items="${orders}">
                        <div class="order-card">
                            <div class="order-header">
                                <div>
                                    <h3>Order #${order.orderId}</h3>
                                    <p class="order-date">${order.orderDate}</p>
                                </div>
                                <div class="order-status">
                                    <span class="status-badge status-${order.status.toLowerCase()}">${order.status}</span>
                                </div>
                            </div>
                            
                            <div class="order-items">
                                <c:forEach var="item" items="${order.items}">
                                    <div class="order-item">
                                        <span>${item.productName} x ${item.quantity}</span>
                                        <span>$${item.subtotal}</span>
                                    </div>
                                </c:forEach>
                            </div>
                            
                            <div class="order-footer">
                                <div>
                                    <p><strong>Total:</strong> $${order.totalAmount}</p>
                                    <p><strong>Payment:</strong> ${order.paymentMethod} (${order.paymentStatus})</p>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
    
    <jsp:include page="footer.jsp" />
</body>
</html>

