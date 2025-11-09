<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Online Shopping Cart - Home</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="container">
        <section class="hero">
            <h1>Welcome to Online Shopping Cart</h1>
            <p>Shop the latest products with ease and convenience</p>
            <c:choose>
                <c:when test="${sessionScope.user != null}">
                    <a href="products" class="btn btn-primary">Start Shopping</a>
                </c:when>
                <c:otherwise>
                    <a href="login.jsp" class="btn btn-primary">Login</a>
                    <a href="register.jsp" class="btn btn-secondary">Register</a>
                </c:otherwise>
            </c:choose>
        </section>
        
        <section class="features">
            <div class="feature-card">
                <h3>🛒 Easy Shopping</h3>
                <p>Browse through our extensive product catalog</p>
            </div>
            <div class="feature-card">
                <h3>🔒 Secure Checkout</h3>
                <p>Safe and secure payment processing</p>
            </div>
            <div class="feature-card">
                <h3>📦 Fast Delivery</h3>
                <p>Quick and reliable order fulfillment</p>
            </div>
        </section>
    </main>
    
    <jsp:include page="footer.jsp" />
</body>
</html>

