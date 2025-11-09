package com.shoppingcart;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.catalina.Context;

import java.io.File;

public class Application {
    private static final int DEFAULT_PORT = 8080;
    
    public static void main(String[] args) {
        Tomcat tomcat = new Tomcat();
        try {
            // Configure port (from environment or default)
            String portEnv = System.getenv("PORT");
            int port = (portEnv != null && !portEnv.isEmpty()) 
                ? Integer.parseInt(portEnv) 
                : DEFAULT_PORT;
            
            tomcat.setPort(port);
            tomcat.getConnector(); // Trigger connector creation
            
            // Set webapp directory
            File webappDir = new File("/app/webapp").getAbsoluteFile();
            if (!webappDir.exists()) {
                webappDir = new File("src/main/webapp").getAbsoluteFile();
            }
            
            System.out.println("Using webapp directory: " + webappDir.getAbsolutePath());
            
            // Create context
            Context context = tomcat.addWebapp("", webappDir.getAbsolutePath());
            
            if (context instanceof StandardContext) {
                ((StandardContext) context).setTldValidation(false);
                ((StandardContext) context).setXmlValidation(false);
            }
            
            // Set up resource root
            WebResourceRoot resources = new StandardRoot(context);
            context.setResources(resources);
            
            // Add classes directory if it exists (for development)
            File classesDir = new File("target/classes");
            if (classesDir.exists()) {
                resources.addPreResources(
                    new DirResourceSet(resources, "/WEB-INF/classes",
                        classesDir.getAbsolutePath(), "/"));
            }
            
            // Basic diagnostics
            System.out.println("\n=== Configuration ===");
            System.out.println("Port: " + port);
            System.out.println("Context path: " + context.getPath());
            System.out.println("Doc base: " + context.getDocBase());
            
            // Verify critical files
            String[] criticalFiles = {
                "register.jsp",
                "login.jsp",
                "index.jsp",
                "WEB-INF/web.xml"
            };
            
            System.out.println("\n=== Resource Check ===");
            for (String file : criticalFiles) {
                File f = new File(webappDir, file);
                System.out.println(file + " exists: " + f.exists());
            }
            
            // Start Tomcat
            tomcat.start();
            System.out.println("\nServer started on port " + port);
            tomcat.getServer().await();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}