<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Online Shopping Cart</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="container">
        <div class="auth-container">
            <div class="auth-card">
                <h2>Login</h2>
                <c:if test="${param.error != null}">
                    <div class="alert alert-error">${param.error}</div>
                </c:if>
                <form action="login" method="post">
                    <div class="form-group">
                        <label for="username">Username</label>
                        <input type="text" id="username" name="username" required>
                    </div>
                    <div class="form-group">
                        <label for="password">Password</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    <button type="submit" class="btn btn-primary btn-block">Login</button>
                </form>
                <p class="auth-link">Don't have an account? <a href="register.jsp">Register here</a></p>
                <div class="demo-credentials">
                    <p><strong>Demo Credentials:</strong></p>
                    <p>Admin: admin / admin123</p>
                    <p>Customer: customer / customer123</p>
                </div>
            </div>
        </div>
    </main>
    
    <jsp:include page="footer.jsp" />
</body>
</html>

