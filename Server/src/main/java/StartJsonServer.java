import com.persistence.EmployeeRepository;
import com.persistence.repository.jdbc.EmployeeRepositoryJdbc;
import com.server.EmployeeServiceImpl;
import com.services.IEmployeeService;
import utils.AbstractServer;
import utils.JsonConcurrentServer;

import java.rmi.ServerException;
import java.util.Properties;

public class StartJsonServer {
    public static final int defaultPort = 12345;

    public static void main(String[] args) {
        Properties serverProps = new Properties();

        EmployeeRepository employeeRepo = new EmployeeRepositoryJdbc(serverProps);
        IEmployeeService serverImpl = new EmployeeServiceImpl(employeeRepo);
        int serverPort = defaultPort;
        try {
            serverPort = Integer.parseInt(serverProps.getProperty("project.server.port"));
        } catch (NumberFormatException nef) {
            System.err.println("Wrong Port Number " + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }
        System.out.println("Starting server on port: " + serverPort);
        AbstractServer server = new JsonConcurrentServer(serverPort, serverImpl);
        try {
            server.start();
        } catch (ServerException e) {
            System.err.println("Error starting the server " + e.getMessage());
        }
    }
}