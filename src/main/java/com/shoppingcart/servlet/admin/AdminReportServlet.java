package com.shoppingcart.servlet.admin;

import com.shoppingcart.dao.OrderDAO;
import com.shoppingcart.service.ReportService;
import com.shoppingcart.util.AuthHelper;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@WebServlet("/admin/reports")
public class AdminReportServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!AuthHelper.requireAdmin(request, response)) {
            return;
        }
        
        String action = request.getParameter("action");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");
        String format = request.getParameter("format");
        
        if ("export".equals(action) && "excel".equals(format)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date start = startDate != null && !startDate.isEmpty() ? sdf.parse(startDate) : 
                            new Date(System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000);
                Date end = endDate != null && !endDate.isEmpty() ? sdf.parse(endDate) : new Date();
                
                byte[] excelData = ReportService.exportToExcel(
                    OrderDAO.getInstance().getOrdersByDateRange(start, end));
                
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename=orders_report.xlsx");
                response.getOutputStream().write(excelData);
                response.getOutputStream().flush();
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (startDate != null && endDate != null && !startDate.isEmpty() && !endDate.isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);
                
                Map<String, Object> report = ReportService.generateSalesReport(start, end);
                request.setAttribute("report", report);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        request.getRequestDispatcher("/admin/reports.jsp").forward(request, response);
    }
}

