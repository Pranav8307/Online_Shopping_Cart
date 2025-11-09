<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<header class="header">
    <div class="container">
        <div class="header-content">
            <a href="index.jsp" class="logo">🛒 ShopCart</a>
            <nav class="nav">
                <a href="index.jsp">Home</a>
                <a href="products">Products</a>
                <c:if test="${sessionScope.user != null}">
                    <a href="cart">Cart</a>
                    <a href="orders">My Orders</a>
                    <a href="profile">Profile</a>
                    <c:if test="${sessionScope.user.role == 'ADMIN'}">
                        <a href="admin/dashboard">Admin</a>
                    </c:if>
                    <a href="logout">Logout</a>
                </c:if>
                <c:if test="${sessionScope.user == null}">
                    <a href="login.jsp">Login</a>
                    <a href="register.jsp">Register</a>
                </c:if>
            </nav>
        </div>
    </div>
</header>

