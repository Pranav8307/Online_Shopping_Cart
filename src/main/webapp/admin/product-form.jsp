<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${product == null ? 'Add' : 'Edit'} Product - Admin</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <main class="container">
        <h1>${product == null ? 'Add New' : 'Edit'} Product</h1>
        
        <form action="products" method="post" class="product-form">
            <input type="hidden" name="action" value="${product == null ? 'create' : 'update'}">
            <c:if test="${product != null}">
                <input type="hidden" name="id" value="${product.id}">
            </c:if>
            
            <div class="form-group">
                <label for="name">Product Name</label>
                <input type="text" id="name" name="name" value="${product.name}" required>
            </div>
            
            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="3" required>${product.description}</textarea>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="price">Price</label>
                    <input type="number" id="price" name="price" step="0.01" value="${product.price}" required>
                </div>
                <div class="form-group">
                    <label for="category">Category</label>
                    <input type="text" id="category" name="category" value="${product.category}" 
                           list="categories" required>
                    <datalist id="categories">
                        <c:forEach var="cat" items="${categories}">
                            <option value="${cat}">
                        </c:forEach>
                    </datalist>
                </div>
                <div class="form-group">
                    <label for="stock">Stock</label>
                    <input type="number" id="stock" name="stock" value="${product.stock}" required>
                </div>
            </div>
            
            <div class="form-group">
                <label for="imageUrl">Image URL (optional)</label>
                <input type="url" id="imageUrl" name="imageUrl" value="${product.imageUrl}">
            </div>
            
            <div class="form-actions">
                <button type="submit" class="btn btn-primary">${product == null ? 'Create' : 'Update'} Product</button>
                <a href="products" class="btn btn-secondary">Cancel</a>
            </div>
        </form>
    </main>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>

