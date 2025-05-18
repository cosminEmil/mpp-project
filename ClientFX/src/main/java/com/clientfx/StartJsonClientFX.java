package com.clientfx;

import com.clientfx.gui.EmployeeController;
import com.services.IEmployeeService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jsonprotocol.ServicesJsonProxy;

import java.io.IOException;
import java.util.Properties;

public class StartJsonClientFX extends Application {
    private static final int defaultPort = 12345;
    private static final String defaultServer = "localHost";

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartJsonClientFX.class.getClassLoader().getResourceAsStream("client.properties"));
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }

        String serverIP =clientProps.getProperty("server.host", defaultServer);
        int serverPort = defaultPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("server.port"));
        } catch (NumberFormatException e) {
            System.err.println("Wrong port number " + e.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        IEmployeeService server = new ServicesJsonProxy(serverIP, serverPort);

        FXMLLoader fxmlLoader = new FXMLLoader(StartJsonClientFX.class.getClassLoader().getResource("employee.fxml"));
        Parent root = fxmlLoader.load();

        EmployeeController employeeController = fxmlLoader.getController();
        employeeController.setServer(server);


        Scene scene = new Scene(root, 800, 700);
        stage.setScene(scene);
        stage.show();
    }
}