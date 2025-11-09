package com.shoppingcart;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import java.io.File;
import java.net.URI;

public class Application {
    private static final int DEFAULT_PORT = 8080;
    private static final String CONTEXT_PATH = "/";
    
    public static void main(String[] args) {
        Tomcat tomcat = null;
        try {
            // Read PORT from environment variable (required by Render)
            // If not set, use default port 8080 for local development
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
            
            // Get webapp directory - check Docker path first, then dev path
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
            
            // Create the context with proper path
            Context context = tomcat.addWebapp(CONTEXT_PATH, webappDir.getAbsolutePath());
            
            // Configure the context
            context.setAddWebinfClassesResources(true);
            context.setReloadable(false); // Disable reloading in production
            
            // Set up resources root
            StandardRoot resourceRoot = new StandardRoot(context);
            context.setResources(resourceRoot);
            
            // Add classes directory if it exists (development mode)
            File classesDir = new File("target/classes");
            if (classesDir.exists()) {
                resourceRoot.addPreResources(
                    new DirResourceSet(resourceRoot, "/WEB-INF/classes",
                        classesDir.getAbsolutePath(), "/"));
                System.out.println("Added classes from: " + classesDir.getAbsolutePath());
            }
            
            System.out.println("Context path: " + context.getPath());
            System.out.println("Document base: " + context.getDocBase());
            
            // Verify critical JSP files exist
            File registerJsp = new File(webappDir, "register.jsp");
            if (!registerJsp.exists()) {
                System.err.println("WARNING: register.jsp not found at: " + registerJsp.getAbsolutePath());
            } else {
                System.out.println("Found register.jsp at: " + registerJsp.getAbsolutePath());
            }
            
            // Configure session timeout programmatically (since web.xml session-config causes issues)
            context.setSessionTimeout(30);
            
            // Note: Filter will be registered after context starts using ServletContainerInitializer
            // For now, we'll skip programmatic filter registration to avoid classloader issues
            
            // Add compiled classes to classpath
            File additionWebInfClasses = new File("target/classes");
            if (additionWebInfClasses.exists()) {
                StandardRoot resources = new StandardRoot(context);
                resources.addPreResources(
                    new DirResourceSet(resources, "/WEB-INF/classes",
                        additionWebInfClasses.getAbsolutePath(), "/"));
                context.setResources(resources);
            }
            
            // Enable JSP support by adding Jasper to parent classloader
            // This is needed for embedded Tomcat
            context.setParentClassLoader(Application.class.getClassLoader());
            
            // Print debug info
            System.out.println("================================================");
            System.out.println("Configuration:");
            System.out.println("Webapp directory: " + webappDir.getAbsolutePath());
            System.out.println("Context path: " + context.getPath());
            System.out.println("Web.xml exists: " + new File(webappDir, "WEB-INF/web.xml").exists());
            System.out.println("index.jsp exists: " + new File(webappDir, "index.jsp").exists());
            System.out.println("Classes directory: " + additionWebInfClasses.getAbsolutePath());
            System.out.println("Classes exist: " + additionWebInfClasses.exists());
            System.out.println("================================================");
            
            // Get and configure the connector BEFORE starting
            // This is crucial - the connector must be created before start()
            Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
            connector.setPort(port);
            connector.setURIEncoding("UTF-8");
            tomcat.setConnector(connector);
            
            // Start the server
            System.out.println("Starting Tomcat server...");
            tomcat.start();
            
            // Note: Filter registration disabled for now due to embedded Tomcat classloader issues
            // Authentication will be handled in individual servlets if needed
            
            // Verify connector is started
            if (connector.getState().isAvailable()) {
                System.out.println("================================================");
                System.out.println("Online Shopping Cart System is running!");
                System.out.println("Access the application at:");
                System.out.println("http://0.0.0.0:" + port + CONTEXT_PATH);
                System.out.println("Port: " + port + " (from " + (portEnv != null ? "environment" : "default") + ")");
                System.out.println("================================================");
                System.out.println("Default Credentials:");
                System.out.println("Admin: admin / admin123");
                System.out.println("Customer: customer / customer123");
                System.out.println("================================================");
                System.out.println("Press Ctrl+C to stop the server");
                System.out.println("================================================");
            } else {
                System.err.println("ERROR: Connector failed to start!");
                System.exit(1);
            }
            
            // Keep the server running
            tomcat.getServer().await();
            
        } catch (LifecycleException e) {
            System.err.println("Failed to start Tomcat server: " + e.getMessage());
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

