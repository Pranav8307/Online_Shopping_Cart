<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products - Online Shopping Cart</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <jsp:include page="header.jsp" />
    
    <main class="container">
        <h1>Product Catalog</h1>
        
        <div class="filters">
            <form method="get" action="products" class="filter-form">
                <input type="text" name="search" placeholder="Search products..." value="${param.search}">
                <select name="category">
                    <option value="">All Categories</option>
                    <c:forEach var="cat" items="${categories}">
                        <option value="${cat}" ${param.category == cat ? 'selected' : ''}>${cat}</option>
                    </c:forEach>
                </select>
                <input type="number" name="minPrice" placeholder="Min Price" value="${param.minPrice}" step="0.01">
                <input type="number" name="maxPrice" placeholder="Max Price" value="${param.maxPrice}" step="0.01">
                <button type="submit" class="btn btn-primary">Filter</button>
                <a href="products" class="btn btn-secondary">Clear</a>
            </form>
        </div>
        
        <div class="products-grid">
            <c:forEach var="product" items="${products}">
                <div class="product-card">
                    <div class="product-image">
                        <c:choose>
                            <c:when test="${not empty product.imageUrl}">
                                <img src="${product.imageUrl}" alt="${product.name}">
                            </c:when>
                            <c:otherwise>
                                <div class="placeholder-image">📦</div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="product-info">
                        <h3>${product.name}</h3>
                        <p class="product-category">${product.category}</p>
                        <p class="product-description">${product.description}</p>
                        <div class="product-footer">
                            <span class="product-price">$${product.price}</span>
                            <c:choose>
                                <c:when test="${product.stock > 0}">
                                    <span class="stock-info">In Stock (${product.stock})</span>
                                    <c:if test="${sessionScope.user != null && sessionScope.user.role == 'CUSTOMER'}">
                                        <form action="cart/add" method="post" class="add-to-cart-form">
                                            <input type="hidden" name="productId" value="${product.id}">
                                            <input type="number" name="quantity" value="1" min="1" max="${product.stock}" style="width: 60px;">
                                            <button type="submit" class="btn btn-primary btn-sm">Add to Cart</button>
                                        </form>
                                    </c:if>
                                </c:when>
                                <c:otherwise>
                                    <span class="stock-info out-of-stock">Out of Stock</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
        
        <c:if test="${empty products}">
            <div class="empty-state">
                <p>No products found. Try adjusting your filters.</p>
            </div>
        </c:if>
    </main>
    
    <jsp:include page="footer.jsp" />
</body>
</html>

