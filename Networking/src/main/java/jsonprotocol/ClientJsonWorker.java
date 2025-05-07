package jsonprotocol;

import com.model.Employee;
import com.services.IObserver;
import com.services.ServicesException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import dto.DTOUtils;

public class ClientJsonWorker implements Runnable, IObserver<Employee, String> {
    private final Socket connection;
    private final IObserver<Employee, String> clientObserver;
    private BufferedReader input;
    private PrintWriter output;
    private final Gson gsonFormatter;
    private volatile boolean connected;
    private final BlockingQueue<IObserver<Employee, String>> observers = new LinkedBlockingQueue<>();

    public ClientJsonWorker(Socket connection, IObserver<Employee, String> clientObserver) {
        this.connection = connection;
        this.clientObserver = clientObserver;
        this.gsonFormatter = new Gson();
        this.connected = true;
        try {
            this.output = new PrintWriter(connection.getOutputStream());
            this.input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error initializing worker streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (connected) {
            try {
                String responseLine = input.readLine();
                if (responseLine == null) {
                    System.out.println("Server disconnected");
                    break;
                }
                System.out.println("Received response: " + responseLine);
                Request response = gsonFormatter.fromJson(responseLine, Request.class);
                handleRequest(response);
                //TODO: Verifica daca responseLine ul contine ResponseType ul si daca numele
                // TODO: numele variabilei
            } catch (IOException e) {
                System.err.println("Reading error in worker: " + e.getMessage());
                break;
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.err.println("Error closing worker connection: " + e.getMessage());
        }
    }

    private void handleResponse(Response response) {
        if (response.getType() == ResponseType.EMPLOYEE_ADDED) {
            Employee employee = DTOUtils.getFromDTO(response.getEmployee());
            try {
                notifyAdd(employee);
                for (IObserver<Employee, String> observer : observers) {
                    observer.notifyAdd(employee);
                }
            } catch (ServicesException e) {
                System.err.println("Error notifying addition: " + e.getMessage());
            }
        } else if (response.getType() == ResponseType.EMPLOYEE_UPDATED) {
            Employee employee = DTOUtils.getFromDTO(response.getEmployee());
            try {
                notifyUpdate(employee, employee.getEmail());
            } catch (ServicesException e) {
                System.err.println("Error notifying update: " + e.getMessage());
            }
        } else if (response.getType() == ResponseType.EMPLOYEE_DELETED) {
            try {
                notifyDelete(response.getEmployee().getEmail());
            } catch (ServicesException e) {
                System.err.println("Error notifying deletion: " + e.getMessage());
            }
        }
    }

    @Override
    public void notifyAdd(Employee entity) throws ServicesException {
        System.out.println("Employee added: " + entity);
        Response response = new Response();
        response.setType(ResponseType.EMPLOYEE_ADDED);
        response.setEmployee(DTOUtils.getDTO(entity));
        sendResponse(response);
    }

    @Override
    public void notifyUpdate(Employee entity, String ID) throws ServicesException {
        System.out.println("Employee updated: " + entity);
        clientObserver.notifyUpdate(entity, ID);
        for (IObserver<Employee, String> observer : observers) {
            observer.notifyUpdate(entity, ID);
        }
    }

    @Override
    public void notifyDelete(String ID) throws ServicesException {
        System.out.println("Employee deleted with ID: " + ID);
        clientObserver.notifyDelete(ID);
        for (IObserver<Employee, String> observer : observers) {
            observer.notifyDelete(ID);
        }
    }

    @Override
    public void addObserver(IObserver<Employee, String> observer) throws ServicesException {
        if (observer == null) {
            throw new ServicesException("Observer cannot be null");
        }
        observers.add(observer);
    }

    public void sendResponse(Response response) throws ServicesException {
        String respLine = gsonFormatter.toJson(response);
        try {
            output.println(respLine);
            output.flush();
        } catch (Exception e) {
            throw new ServicesException("Error sending response: " + e.getMessage());
        }
    }

    public void stop() {
        connected = false;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
        } catch (IOException e) {
            System.err.println("Error closing worker resources: " + e.getMessage());
        }
    }
}