package com.clientfx.gui;

import com.model.Employee;
import com.services.IEmployeeService;
import com.services.IObserver;
import com.services.ServicesException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeController implements Initializable, IObserver<Employee, String> {
    @FXML private TableView<Employee> employeeTable;
    @FXML private TableColumn<Employee, String> nameColumn;
    @FXML private TableColumn<Employee, String> emailColumn;

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label statusLabel;

    private IEmployeeService server;
    private Employee employee;
    private ObservableList<Employee> employeeData = FXCollections.observableArrayList();

    public EmployeeController() {
        System.out.println("Constructor EmployeeController");
    }

    public EmployeeController(IEmployeeService server) {
        this.server = server;
        System.out.println("Constructor EmployeeController cu server param");
    }

    public void setServer(IEmployeeService s) {
        server = s;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void loadEmployeeData() {}

    @Override
    public void notifyCrudOperation(Employee entity, String operationType) throws ServicesException {}

    @FXML
    public void handleAddEmployee() {}

    @FXML
    public void handleUpdateEmployee() {}

    @FXML
    public void handleDeleteEmployee() {}

    @FXML
    public void handleClearFields() {
        clearFields();
        statusLabel.setText("");
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
    }
}