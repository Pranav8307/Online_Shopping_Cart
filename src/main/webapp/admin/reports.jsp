<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Reports & Analytics - Admin</title>
    <link rel="stylesheet" href="../css/style.css">
</head>
<body>
    <jsp:include page="../header.jsp" />
    
    <main class="container">
        <h1>Reports & Analytics</h1>
        
        <div class="report-filters">
            <form method="get" action="reports" class="filter-form">
                <div class="form-row">
                    <div class="form-group">
                        <label for="startDate">Start Date</label>
                        <input type="date" id="startDate" name="startDate" value="${param.startDate}">
                    </div>
                    <div class="form-group">
                        <label for="endDate">End Date</label>
                        <input type="date" id="endDate" name="endDate" value="${param.endDate}">
                    </div>
                    <div class="form-group">
                        <label>&nbsp;</label>
                        <button type="submit" class="btn btn-primary">Generate Report</button>
                        <a href="reports?action=export&format=excel&startDate=${param.startDate}&endDate=${param.endDate}" 
                           class="btn btn-secondary">Export to Excel</a>
                    </div>
                </div>
            </form>
        </div>
        
        <c:if test="${not empty report}">
            <div class="report-summary">
                <h2>Sales Summary</h2>
                <div class="summary-cards">
                    <div class="summary-card">
                        <h3>Total Revenue</h3>
                        <p class="summary-value">$${report.totalRevenue}</p>
                    </div>
                    <div class="summary-card">
                        <h3>Total Orders</h3>
                        <p class="summary-value">${report.totalOrders}</p>
                    </div>
                </div>
                
                <h2>Sales by Category</h2>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Category</th>
                            <th>Quantity Sold</th>
                            <th>Revenue</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${report.salesByCategory}">
                            <tr>
                                <td>${entry.key}</td>
                                <td>${entry.value}</td>
                                <td>$${report.revenueByCategory[entry.key]}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                
                <h2>Top Products</h2>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>Product</th>
                            <th>Quantity Sold</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="entry" items="${report.salesByProduct}">
                            <tr>
                                <td>${entry.key}</td>
                                <td>${entry.value}</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:if>
    </main>
    
    <jsp:include page="../footer.jsp" />
</body>
</html>

