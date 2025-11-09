<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - Online Shopping Cart</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="container">
        <h1>Checkout</h1>
        
        <c:if test="${not empty error}">
            <div class="alert alert-error">${error}</div>
        </c:if>
        
        <div class="checkout-container">
            <div class="checkout-form">
                <h2>Shipping Information</h2>
                <form action="checkout" method="post">
                    <div class="form-group">
                        <label for="shippingAddress">Shipping Address</label>
                        <textarea id="shippingAddress" name="shippingAddress" rows="3" required>${user.address}, ${user.city}, ${user.state} ${user.zipCode}</textarea>
                    </div>
                    
                    <div class="form-group">
                        <label for="paymentMethod">Payment Method</label>
                        <select id="paymentMethod" name="paymentMethod" required>
                            <option value="Credit Card">Credit Card</option>
                            <option value="Debit Card">Debit Card</option>
                            <option value="PayPal">PayPal</option>
                            <option value="Cash on Delivery">Cash on Delivery</option>
                        </select>
                    </div>
                    
                    <h2>Order Summary</h2>
                    <div class="order-summary">
                        <table class="summary-table">
                            <c:forEach var="item" items="${cart}">
                                <tr>
                                    <td>${item.product.name} x ${item.quantity}</td>
                                    <td>$${item.subtotal}</td>
                                </tr>
                            </c:forEach>
                            <tr class="total-row">
                                <td><strong>Total</strong></td>
                                <td><strong>$${total}</strong></td>
                            </tr>
                        </table>
                    </div>
                    
                    <button type="submit" class="btn btn-primary btn-block">Place Order</button>
                </form>
            </div>
        </div>
    </main>
    
    <jsp:include page="footer.jsp" />
</body>
</html>

