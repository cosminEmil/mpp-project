package com.server;

import com.persistence.EmployeeRepository;
import com.persistence.repository.jdbc.EmployeeRepositoryJdbc;
import com.services.IEmployeeService;
import utils.AbstractServer;
import utils.JsonConcurrentServer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StartJsonServer {
    private static final int DEFAULT_PORT = 12345;

    public static void main(String[] args) {
        try {
            System.out.println("Starting server initialization...");

            // 1. Load configuration
            Properties serverProps = loadServerProperties();

            // 2. Initialize database repository
            System.out.println("Initializing database repository...");
            EmployeeRepository employeeRepo = new EmployeeRepositoryJdbc(serverProps);

            // 3. Create service implementation
            System.out.println("Creating employee service...");
            EmployeeServiceImpl employeeService = new EmployeeServiceImpl(employeeRepo);

            // 4. Get server port
            int serverPort = getServerPort(serverProps);
            System.out.println("Starting server on port: " + serverPort);

            // 5. Start server
            AbstractServer server = new JsonConcurrentServer(serverPort, employeeService);
            server.start();

            System.out.println("Server started successfully on port " + serverPort);
        } catch (Exception e) {
            System.err.println("SERVER STARTUP FAILED:");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static Properties loadServerProperties() throws IOException {
        Properties props = new Properties();
        try (InputStream input = StartJsonServer.class.getResourceAsStream("/server.properties")) {
            if (input == null) {
                throw new IOException("server.properties not found in classpath");
            }
            props.load(input);
            System.out.println("Server properties loaded successfully");
            return props;
        }
    }

    private static int getServerPort(Properties props) {
        String portStr = props.getProperty("server.port");
        try {
            return portStr != null ? Integer.parseInt(portStr) : DEFAULT_PORT;
        } catch (NumberFormatException e) {
            System.err.println("Invalid port configuration, using default: " + DEFAULT_PORT);
            return DEFAULT_PORT;
        }
    }
}