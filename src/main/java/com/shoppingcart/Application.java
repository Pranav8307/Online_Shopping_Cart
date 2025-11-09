package com.shoppingcart;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;

public class Application {
    private static final int DEFAULT_PORT = 8080;
    private static final String CONTEXT_PATH = "";
    
    public static void main(String[] args) {
        Tomcat tomcat = null;
        try {
            // Read PORT from environment variable (required by Render)
            String portEnv = System.getenv("PORT");
            int port = (portEnv != null && !portEnv.isEmpty()) 
                ? Integer.parseInt(portEnv) 
                : DEFAULT_PORT;
            
            tomcat = new Tomcat();
            tomcat.setPort(port);
            // Use 0.0.0.0 to bind to all interfaces (required for Render)
            tomcat.setHostname("0.0.0.0");
            
            // Set base directory for Tomcat
            String baseDir = new File("target/tomcat").getAbsolutePath();
            tomcat.setBaseDir(baseDir);
            
            // Get webapp directory
            File webappDir = new File("/app/webapp").getAbsoluteFile();
            if (!webappDir.exists()) {
                // Fallback to dev path
                webappDir = new File("src/main/webapp").getAbsoluteFile();
                if (!webappDir.exists()) {
                    System.err.println("ERROR: Webapp directory not found at either /app/webapp or src/main/webapp");
                    System.exit(1);
                }
            }
            
            System.out.println("Using webapp directory: " + webappDir.getAbsolutePath());
            
            // Set up the web app context
            Context context = tomcat.addWebapp(CONTEXT_PATH, webappDir.getAbsolutePath());
            context.setAddWebinfClassesResources(true);
            context.setReloadable(false); // Disable reloading in production
            
            // Configure resources
            StandardRoot resources = new StandardRoot(context);
            context.setResources(resources);
            
            // Add classes directory if it exists
            File classesDir = new File("target/classes");
            if (classesDir.exists()) {
                resources.addPreResources(
                    new DirResourceSet(resources, "/WEB-INF/classes",
                        classesDir.getAbsolutePath(), "/"));
                System.out.println("Added classes from: " + classesDir.getAbsolutePath());
            }
            
            // Configure JSP servlet
            Tomcat.addServlet(context, "jsp", "org.apache.jasper.servlet.JspServlet");
            context.addServletMappingDecoded("*.jsp", "jsp");
            
            // Configure default servlet
            Tomcat.addServlet(context, "default", "org.apache.catalina.servlets.DefaultServlet");
            context.addServletMappingDecoded("/", "default");
            
            // Verify critical files
            System.out.println("\n=== Resource Check ===");
            String[] criticalFiles = {
                "register.jsp",
                "login.jsp",
                "index.jsp",
                "WEB-INF/web.xml"
            };
            
            for (String file : criticalFiles) {
                File f = new File(webappDir, file);
                System.out.println(file + " exists: " + f.exists() + " at " + f.getAbsolutePath());
            }
            
            // Configure and start connector
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setPort(port);
            connector.setURIEncoding("UTF-8");
            tomcat.setConnector(connector);
            
            // Start the server
            System.out.println("\n=== Starting Server ===");
            tomcat.start();
            
            if (connector.getState().isAvailable()) {
                System.out.println("\n=== Server Started Successfully ===");
                System.out.println("Access the application at:");
                System.out.println("http://0.0.0.0:" + port);
                System.out.println("\nTo verify configuration, visit:");
                System.out.println("http://0.0.0.0:" + port + "/test.html");
                System.out.println("\nPress Ctrl+C to stop the server");
            } else {
                System.err.println("ERROR: Connector failed to start!");
                System.exit(1);
            }
            
            tomcat.getServer().await();
            
        } catch (LifecycleException e) {
            System.err.println("Failed to start Tomcat: " + e.getMessage());
            e.printStackTrace();
            if (tomcat != null) {
                try {
                    tomcat.stop();
                } catch (Exception ex) {
                    // Ignore
                }
            }
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            if (tomcat != null) {
                try {
                    tomcat.stop();
                } catch (Exception ex) {
                    // Ignore
                }
            }
            System.exit(1);
        }
    }
}