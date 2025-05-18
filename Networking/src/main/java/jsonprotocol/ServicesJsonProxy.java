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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;
import dto.DTOUtils;
import dto.EmployeeDTO;

public class ServicesJsonProxy implements IEmployeeService {
    private final String host;
    private final int port;

    private IObserver<Employee, String> client;

    private BufferedReader input;
    private PrintWriter output;
    private Gson gsonFormatter;
    private Socket connection;

    private final BlockingQueue<Response> responses;
    private volatile boolean finished;

    public ServicesJsonProxy(String host, int port) {
        this.host = host;
        this.port = port;
        responses = new LinkedBlockingQueue<Response>();
    }

    private void initializeConnection() throws ServicesException {
        try {
            gsonFormatter = new Gson();
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream());
            output.flush();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            throw new ServicesException("Connection error: " + e.getMessage());
        }
    }

    private void sendRequest(Request request) throws ServicesException {
        String reqLine = gsonFormatter.toJson(request);
        try {
            output.println(reqLine);
            output.flush();
        } catch (Exception e) {
            throw new ServicesException("Error sending request: " + e.getMessage());
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }


    @Override
    public void addObserver(IObserver<Employee, String> observer) throws ServicesException {
        if (observer == null) {
            throw new ServicesException("Observer cannot be null");
        }
        this.client = observer;
    }

    private void checkConnection() throws ServicesException {
        if (connection == null || connection.isClosed() || !connection.isConnected()) {
            initializeConnection();
        }
    }

    @Override
    public void addEmployee(Employee employee) throws ServicesException {
        checkConnection();
        Request req = JsonProtocolUtils.createAddRequest(employee);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.EMPLOYEE_ADDED) {

        }
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new ServicesException(err);
        }
    }

    @Override
    public void deleteEmployee(Employee employee) throws ServicesException {
        // Presupunem că Employee are un constructor care primește doar email
        checkConnection();
        Request req = JsonProtocolUtils.createDeleteRequest(employee);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new ServicesException(err);
        }
        if (response.getType() == ResponseType.EMPLOYEE_DELETED) {
            System.out.println("Employee successfully deleted.");
        }
    }

    @Override
    public void updateEmployee(String email, Employee employee) throws ServicesException {
        checkConnection();
        Request req = JsonProtocolUtils.createUpdateRequest(employee);
        sendRequest(req);
        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            String err = response.getErrorMessage();
            throw new ServicesException(err);
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws ServicesException {
        checkConnection();
        Request request = new Request();
        request.setType(RequestType.GET_ALL_EMPLOYEES);
        sendRequest(request);

        Response response = readResponse();
        if (response.getType() == ResponseType.ERROR) {
            throw new ServicesException(response.getErrorMessage());
        }

        List<EmployeeDTO> employeeDTOs = response.getEmployees();
        List<Employee> employees = new ArrayList<>();
        for (EmployeeDTO dto : employeeDTOs) {
            employees.add(DTOUtils.getFromDTO(dto));
        }

        return employees;
    }
    private Response readResponse() throws ServicesException {
        try {
            return responses.take();
        } catch (InterruptedException e) {
            throw new ServicesException("Error reading response: " + e.getMessage());
        }
    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.EMPLOYEE_ADDED ||
                response.getType() == ResponseType.EMPLOYEE_UPDATED ||
                response.getType() == ResponseType.EMPLOYEE_DELETED;
    }

    private void handleUpdate(Response response) {
        // Verifică dacă răspunsul conține un obiect employeeDTO
        if (response.getEmployee() != null) {
            EmployeeDTO employeeDTO = response.getEmployee();
            Employee employee = DTOUtils.getFromDTO(employeeDTO);
            switch (response.getType()) {
                case EMPLOYEE_ADDED:
                    try {
                        client.notifyAdd(employee);
                    } catch (ServicesException e) {
                        System.err.println("Error notifying addition: " + e.getMessage());
                    }
                    break;
                case EMPLOYEE_UPDATED:
                    try {
                        client.notifyUpdate(employee, employee.getEmail());
                    } catch (ServicesException e) {
                        System.err.println("Error notifying update: " + e.getMessage());
                    }
                    break;
                case EMPLOYEE_DELETED:
                    // În cazul unei ștergeri, nu mai avem obiectul employee, doar email-ul
                    String email = response.getEmployeeEmail();  // Asigură-te că ai metoda getEmployeeEmail()
                    try {
                        client.notifyDelete(email);  // Trimite doar email-ul
                    } catch (ServicesException e) {
                        System.err.println("Error notifying delete: " + e.getMessage());
                    }
                    break;
            }
        } else {
            // Dacă răspunsul nu conține employeeDTO, dar este de tip EMPLOYEE_DELETED
            if (response.getType() == ResponseType.EMPLOYEE_DELETED) {
                // Aici presupunem că răspunsul conține doar email-ul angajatului șters
                String email = response.getEmployeeEmail(); // presupunem că există un asemenea câmp
                try {
                    client.notifyDelete(email);
                } catch (ServicesException e) {
                    System.err.println("Error notifying delete: " + e.getMessage());
                }
            }
        }
    }


    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while (!finished) {
                try {
                    String responseLine = input.readLine();
                    if (responseLine == null) {
                        System.out.println("Server closed connection.");
                        break;
                    }
                    System.out.println("[CLIENT] Response received: " + responseLine);

                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    System.out.println("[CLIENT] Parsed response type: " + response.getType());

                    if (isUpdate(response)) {
                        System.out.println("[CLIENT] Handling update...");
                        handleUpdate(response);
                    } else {
                        System.out.println("[CLIENT] Handling direct response...");
                        responses.put(response);
                    }

                } catch (IOException | InterruptedException e) {
                    System.err.println("[CLIENT] Reading error: " + e.getMessage());
                    break;
                }
            }
        }
    }
}
