package utils;

import com.model.Employee;
import com.services.IEmployeeService;
import com.services.IObserver;
import jsonprotocol.ClientJsonWorker;

import java.net.Socket;

public class JsonConcurrentServer extends AbsConcurrentServer {
    private IObserver<Employee, String> employeeServer;

    public JsonConcurrentServer(int port, IObserver<Employee, String> employeeServer) {
        super(port);
        this.employeeServer = employeeServer;
        System.out.println("Employee- JsonConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        ClientJsonWorker worker = new ClientJsonWorker(client, employeeServer);
        employeeServer.addObserver(worker);
        Thread tw = new Thread(worker);
        return tw;
    }
}
