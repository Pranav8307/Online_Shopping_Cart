package com.shoppingcart.servlet;

import javax.servlet.ServletContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet("/diagnostic")
public class DiagnosticServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        
        out.println("=== Diagnostic Information ===");
        out.println("\nRequest Information:");
        out.println("Request URI: " + request.getRequestURI());
        out.println("Context Path: " + request.getContextPath());
        out.println("Servlet Path: " + request.getServletPath());
        out.println("Path Info: " + request.getPathInfo());
        
        ServletContext context = getServletContext();
        out.println("\nContext Information:");
        out.println("Context Path: " + context.getContextPath());
        out.println("Real Path of /: " + context.getRealPath("/"));
        out.println("Real Path of /register.jsp: " + context.getRealPath("/register.jsp"));
        out.println("Real Path of /WEB-INF: " + context.getRealPath("/WEB-INF"));
        
        out.println("\nResource Exists Check:");
        String[] criticalResources = {
            "/register.jsp",
            "/login.jsp",
            "/index.jsp",
            "/WEB-INF/web.xml"
        };
        
        for (String path : criticalResources) {
            try {
                boolean exists = context.getResource(path) != null;
                File realFile = new File(context.getRealPath(path));
                out.println(String.format("%s: Resource exists=%s, File exists=%s, Real path=%s",
                    path, exists, realFile.exists(), realFile.getAbsolutePath()));
            } catch (Exception e) {
                out.println(path + ": Error checking - " + e.getMessage());
            }
        }
        
        out.println("\nAvailable Servlets:");
        Set<String> mappings = context.getServletRegistrations().keySet();
        for (String mapping : mappings) {
            out.println("- " + mapping);
        }
        
        // Print environment variables
        out.println("\nEnvironment Variables:");
        out.println("PORT: " + System.getenv("PORT"));
        out.println("JAVA_HOME: " + System.getenv("JAVA_HOME"));
        out.println("Working Directory: " + System.getProperty("user.dir"));
        
        // Print classpath
        out.println("\nClasspath:");
        String classpath = System.getProperty("java.class.path");
        for (String path : classpath.split(File.pathSeparator)) {
            out.println("- " + path);
        }
    }
}