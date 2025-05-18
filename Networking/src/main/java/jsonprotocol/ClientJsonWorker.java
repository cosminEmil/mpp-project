package jsonprotocol;

import com.model.Employee;
import com.services.IEmployeeService;
import com.services.IObserver;
import com.services.ServicesException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import dto.DTOUtils;
import dto.EmployeeDTO;

public class ClientJsonWorker implements Runnable, IObserver<Employee, String> {
    private final IEmployeeService server;
    private final Socket connection;

    private IObserver<Employee, String> clientObserver;
    private BufferedReader input;
    private PrintWriter output;
    private final Gson gsonFormatter;
    private final BlockingQueue<IObserver<Employee, String>> observers = new LinkedBlockingQueue<>();

    public ClientJsonWorker(IEmployeeService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        this.gsonFormatter = new Gson();
        try {
            this.output = new PrintWriter(connection.getOutputStream());
            this.input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            System.err.println("Error initializing worker streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String responseLine = input.readLine();
                if (responseLine == null) {
                    System.out.println("Server disconnected");
                    break;
                }
                System.out.println("Received request: " + responseLine);
                Request request = gsonFormatter.fromJson(responseLine, Request.class);
                Response response = handleRequest(request);

                if (response != null) {
                    sendResponse(response);
                }

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

        Response response = new Response();
        response.setType(ResponseType.EMPLOYEE_UPDATED);
        response.setEmployee(DTOUtils.getDTO(entity));
        sendResponse(response);
    }


    @Override
    public void notifyDelete(String email) throws ServicesException {
        System.out.println("Employee deleted with email: " + email);

        Response response = new Response();
        response.setType(ResponseType.EMPLOYEE_DELETED);
        System.out.println("Email sent in the response: " + email);
        response.setEmployeeEmail(email);
        sendResponse(response);
    }


    @Override
    public void addObserver(IObserver<Employee, String> observer) throws ServicesException {
        if (observer == null) {
            throw new ServicesException("Observer cannot be null");
        }
        observers.add(observer);
    }

    private static final Response okResponse = JsonProtocolUtils.createOkResponse();

    private Response handleRequest(Request request) {
        switch (request.getType()) {
            case RequestType.ADD_EMPLOYEE:
                System.out.println("Add request ... " + request.getType());
                EmployeeDTO employeeDTO = request.getEmployee();
                Employee employee = DTOUtils.getFromDTO(employeeDTO);
                try {
                    server.addEmployee(employee);
                    return okResponse;
                } catch (ServicesException e) {
                    return JsonProtocolUtils.createErrorResponse(e.getMessage());
                }

            case RequestType.UPDATE_EMPLOYEE:
                System.out.println("Update request ... " + request.getType());
                EmployeeDTO employeeDTO1 = request.getEmployee();
                Employee employee1 = DTOUtils.getFromDTO(employeeDTO1);

                try {
                    server.updateEmployee(employee1.getEmail(), employee1);
                    return okResponse;
                } catch (ServicesException e) {
                    return JsonProtocolUtils.createErrorResponse(e.getMessage());
                }
            case RequestType.DELETE_EMPLOYEE:
                System.out.println("Delete request ... " + request.getType());
                EmployeeDTO employeeDTO2 = request.getEmployee();
                Employee employee2 = DTOUtils.getFromDTO(employeeDTO2);

                try {
                    server.deleteEmployee(employee2);
                    return okResponse;
                } catch (ServicesException e) {
                    return JsonProtocolUtils.createErrorResponse(e.getMessage());
                }
            case GET_ALL_EMPLOYEES:
                try {
                    List<Employee> all = server.getAllEmployees();
                    List<EmployeeDTO> dtos = all.stream().map(DTOUtils::getDTO).toList();

                    Response resp = new Response();
                    resp.setType(ResponseType.OK);
                    resp.setEmployees(dtos);
                    return resp;
                } catch (ServicesException e) {
                    return JsonProtocolUtils.createErrorResponse(e.getMessage());
                }

        }
        return null;
    }

    public void sendResponse(Response response) throws ServicesException {
        String respLine = gsonFormatter.toJson(response);
        System.out.println("Sending response " + respLine);
        synchronized (output) {
            output.println(respLine);
            output.flush();
        }
    }


}