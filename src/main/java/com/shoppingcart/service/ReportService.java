package com.shoppingcart.service;

import com.shoppingcart.model.Order;
import com.shoppingcart.model.OrderItem;
import com.shoppingcart.model.Product;
import com.shoppingcart.dao.OrderDAO;
import com.shoppingcart.dao.ProductDAO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class ReportService {
    
    public static Map<String, Object> generateSalesReport(Date startDate, Date endDate) {
        Map<String, Object> report = new HashMap<>();
        List<Order> orders = OrderDAO.getInstance().getOrdersByDateRange(startDate, endDate);
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalOrders = orders.size();
        Map<String, Integer> salesByCategory = new HashMap<>();
        Map<String, Integer> salesByProduct = new HashMap<>();
        Map<String, BigDecimal> revenueByCategory = new HashMap<>();
        
        for (Order order : orders) {
            if ("COMPLETED".equalsIgnoreCase(order.getPaymentStatus())) {
                totalRevenue = totalRevenue.add(order.getTotalAmount());
                
                for (OrderItem item : order.getItems()) {
                    Product product = ProductDAO.getInstance().getProductById(item.getProductId());
                    if (product != null) {
                        String category = product.getCategory();
                        salesByCategory.put(category, 
                            salesByCategory.getOrDefault(category, 0) + item.getQuantity());
                        revenueByCategory.put(category,
                            revenueByCategory.getOrDefault(category, BigDecimal.ZERO)
                                .add(item.getSubtotal()));
                        
                        salesByProduct.put(item.getProductName(),
                            salesByProduct.getOrDefault(item.getProductName(), 0) + item.getQuantity());
                    }
                }
            }
        }
        
        report.put("totalRevenue", totalRevenue);
        report.put("totalOrders", totalOrders);
        report.put("salesByCategory", salesByCategory);
        report.put("salesByProduct", salesByProduct);
        report.put("revenueByCategory", revenueByCategory);
        report.put("orders", orders);
        
        return report;
    }
    
    public static byte[] exportToExcel(List<Order> orders) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Orders Report");
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Order ID", "User ID", "Order Date", "Status", "Total Amount", 
                           "Payment Method", "Payment Status", "Shipping Address"};
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // Create data rows
        int rowNum = 1;
        for (Order order : orders) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(order.getOrderId());
            row.createCell(1).setCellValue(order.getUserId());
            row.createCell(2).setCellValue(order.getOrderDate().toString());
            row.createCell(3).setCellValue(order.getStatus());
            row.createCell(4).setCellValue(order.getTotalAmount().doubleValue());
            row.createCell(5).setCellValue(order.getPaymentMethod());
            row.createCell(6).setCellValue(order.getPaymentStatus());
            row.createCell(7).setCellValue(order.getShippingAddress());
        }
        
        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }
}

