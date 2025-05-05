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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.google.gson.Gson;

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

    @Override
    public void addEmployee(Employee employee) throws ServicesException {
        initializeConnection();
    }

    private void initializeConnection() {

        try {
            gsonFormatter = new Gson();
            connection = new Socket(host, port);
            output = new PrintWriter(connection.getOutputStream());
            output.flush();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    @Override
    public void deleteEmployee(String email) throws ServicesException {

    }

    @Override
    public void updateEmployee(String email, Employee employee) throws ServicesException {

    }

    private boolean isUpdate(Response response) {
        return response.getType() == ResponseType.EMPLOYEE_ADDED || response.getType() == ResponseType.EMPLOYEE_UPDATED
    }

    private class ReaderThread implements Runnable {
        @Override
        public void run() {
            while(!finished) {
                try {
                    String responseLine = input.readLine();
                    System.out.println("response received" + responseLine);
                    Response response = gsonFormatter.fromJson(responseLine, Response.class);
                    if (isUpdate(response)) {
                        handleResponse(response);
                    } else {
                        try {
                            responses.put(response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error" + e);
                }
            }
        }
    }
}
