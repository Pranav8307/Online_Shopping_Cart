<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - Online Shopping Cart</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="container">
        <h1>Shopping Cart</h1>
        
        <c:choose>
            <c:when test="${empty cart}">
                <div class="empty-state">
                    <p>Your cart is empty. <a href="products">Start shopping!</a></p>
                </div>
            </c:when>
            <c:otherwise>
                <div class="cart-container">
                    <table class="cart-table">
                        <thead>
                            <tr>
                                <th>Product</th>
                                <th>Price</th>
                                <th>Quantity</th>
                                <th>Subtotal</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cart}">
                                <tr>
                                    <td>
                                        <strong>${item.product.name}</strong>
                                        <p class="product-category">${item.product.category}</p>
                                    </td>
                                    <td>$${item.product.price}</td>
                                    <td>
                                        <form action="cart/update" method="post" class="inline-form">
                                            <input type="hidden" name="productId" value="${item.product.id}">
                                            <input type="number" name="quantity" value="${item.quantity}" 
                                                   min="1" max="${item.product.stock}" onchange="this.form.submit()">
                                        </form>
                                    </td>
                                    <td>$${item.subtotal}</td>
                                    <td>
                                        <form action="cart/remove" method="post" class="inline-form">
                                            <input type="hidden" name="productId" value="${item.product.id}">
                                            <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr>
                                <td colspan="3"><strong>Total</strong></td>
                                <td><strong>$${total}</strong></td>
                                <td></td>
                            </tr>
                        </tfoot>
                    </table>
                    
                    <div class="cart-actions">
                        <a href="products" class="btn btn-secondary">Continue Shopping</a>
                        <a href="checkout" class="btn btn-primary">Proceed to Checkout</a>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </main>
    
    <jsp:include page="footer.jsp" />
</body>
</html>

