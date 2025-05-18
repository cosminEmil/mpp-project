package com.clientfx.gui;

import com.model.Employee;
import com.services.IEmployeeService;
import com.services.IObserver;
import com.services.ServicesException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private Employee currentEmployee;
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
        try {
            if (server != null) {
                server.addObserver(this); // Înregistrează-te ca observer
            }
        } catch (ServicesException e) {
            showError("Failed to register as observer: " + e.getMessage());
        }
        loadEmployeeData();
    }

    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configure table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Add listener for table selection
        employeeTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showEmployeeDetails(newValue));
    }

    private void loadEmployeeData() {
        try {
            employeeData.clear();
            employeeData.addAll(server.getAllEmployees());
            employeeTable.setItems(employeeData);
        } catch (Exception e) {
            showError("Error loading employee data: " + e.getMessage());
        }
    }


    private void showEmployeeDetails(Employee employee) {
        if (employee != null) {
            nameField.setText(employee.getName());
            emailField.setText(employee.getEmail());
            passwordField.setText(employee.getPassword());
            currentEmployee = employee;
        } else {
            clearFields();
            currentEmployee = null;
        }
    }

    @FXML
    public void handleAddEmployee() {
        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError("All fields must be filled!");
                return;
            }

            Employee newEmployee = new Employee(name, email, password);
            server.addEmployee(newEmployee);

            statusLabel.setText("Employee added successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        } catch (ServicesException e) {
            showError("Error adding employee: " + e.getMessage());
        }
    }

    @FXML
    public void handleUpdateEmployee() {
        if (currentEmployee == null) {
            showError("No employee selected!");
            return;
        }

        try {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                showError("All fields must be filled!");
                return;
            }

            Employee updatedEmployee = new Employee(name, email, password);
            server.updateEmployee(currentEmployee.getEmail(), updatedEmployee);

            statusLabel.setText("Employee updated successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        } catch (ServicesException e) {
            showError("Error updating employee: " + e.getMessage());
        }
    }

    @FXML
    public void handleDeleteEmployee() {
        Employee selected = employeeTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("No employee selected!");
            return;
        }

        System.out.println("Deleting: " + selected.getName() + ", " + selected.getEmail());

        try {
            server.deleteEmployee(selected);  // Trimit întregul obiect
            statusLabel.setText("Employee deleted successfully!");
            statusLabel.setStyle("-fx-text-fill: green;");
            clearFields();
        } catch (ServicesException e) {
            showError("Error deleting employee: " + e.getMessage());
        }
    }


    @FXML
    public void handleClearFields() {
        clearFields();
        statusLabel.setText("");
    }

    private void clearFields() {
        nameField.clear();
        emailField.clear();
        passwordField.clear();
        currentEmployee = null;
        employeeTable.getSelectionModel().clearSelection();
    }

    private void showError(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: red;");
    }

    @Override
    public void notifyAdd(Employee entity) throws ServicesException {
        Platform.runLater(() -> {
            employeeData.add(entity);
            statusLabel.setText("New employee added: " + entity.getName());
            statusLabel.setStyle("-fx-text-fill: green;");
        });
    }

    @Override
    public void notifyUpdate(Employee entity, String ID) throws ServicesException {
        Platform.runLater(() -> {
            for (int i = 0; i < employeeData.size(); i++) {
                if (employeeData.get(i).getEmail().equals(ID)) {
                    employeeData.set(i, entity);
                    break;
                }
            }
            statusLabel.setText("Employee updated: " + entity.getName());
            statusLabel.setStyle("-fx-text-fill: green;");
        });
    }

    @Override
    public void notifyDelete(String ID) throws ServicesException {
        Platform.runLater(() -> {
            // Actualizează UI-ul pentru a elimina angajatul din tabelă
            employeeData.removeIf(e -> e.getEmail().equals(ID));  // Verifică dacă email-ul este corect
            statusLabel.setText("Employee deleted: " + ID);
            statusLabel.setStyle("-fx-text-fill: green;");
        });
    }


    @Override
    public void addObserver(IObserver<Employee, String> observer) throws ServicesException {

    }
}