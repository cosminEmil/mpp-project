package com.server;

import com.model.Employee;
import com.persistence.EmployeeRepository;
import com.services.IEmployeeService;
import com.services.IObserver;
import com.services.ServicesException;

import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements IEmployeeService {
    private final EmployeeRepository employeeRepository;
    private final List<IObserver<Employee, String>> observers = new ArrayList<>();

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // ClientController sau ClientWorker adauga un observer
    public void addObserver(IObserver<Employee, String> observer) {
        observers.add(observer);
    }

    // Helper pentru notificare
    private void notifyObservers(Employee employee, String operationType) {
        for (IObserver<Employee, String> observer : observers) {
            observer.notifyCrudOperation(employee, operationType);
        }
    }

    @Override
    public void addEmployee(Employee employee) throws ServicesException {
        try {
            // Check if employee already exists
            if (employeeRepository.findBy(employee.getEmail()) != null) {
                throw new ServicesException("Employee with email " + employee.getEmail() + " already exists");
            }

            employeeRepository.save(employee);
            notifyObservers(employee, "ADD");
        } catch (Exception e) {
            throw new ServicesException("Failed to add employee: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteEmployee(String email) throws ServicesException {
        try {
            Employee employee = employeeRepository.findBy(email);
            if (employee == null) {
                throw new ServicesException("Employee with email " + email + " not found");
            }

            employeeRepository.delete(email);
            notifyObservers(employee, "DELETE");
        } catch (Exception e) {
            throw new ServicesException("Failed to delete employee: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateEmployee(String email, Employee employee) throws ServicesException {
        try {
            // Verify the employee exists
            if (employeeRepository.findBy(email) == null) {
                throw new ServicesException("Employee with email " + email + " not found");
            }

            // Verify the update doesn't conflict with existing emails
            if (!email.equals(employee.getEmail())) {
                if (employeeRepository.findBy(employee.getEmail()) != null) {
                    throw new ServicesException("New email already exists");
                }
            }

            employeeRepository.update(email, employee);
            notifyObservers(employee, "UPDATE");
        } catch (Exception e) {
            throw new ServicesException("Failed to update employee: " + e.getMessage(), e);
        }
    }
    /*
    @Override
    public Employee findEmployeeByEmail(String email) throws ServicesException {
        try {
            Employee employee = employeeRepository.findBy(email);
            if (employee == null) {
                throw new ServicesException("Employee not found");
            }
            return employee;
        } catch (Exception e) {
            throw new ServicesException("Failed to find employee: " + e.getMessage(), e);
        }
    }

    @Override
    public Employee findEmployeeByNameAndEmail(String name, String email) throws ServicesException {
        try {
            Employee employee = employeeRepository.findBy(name, email);
            if (employee == null) {
                throw new ServicesException("Employee not found");
            }
            return employee;
        } catch (Exception e) {
            throw new ServicesException("Failed to find employee: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws ServicesException {
        try {
            List<Employee> employees = (List<Employee>) employeeRepository.getAll();
            return employees;
        } catch (Exception e) {
            throw new ServicesException("Failed to get all employees: " + e.getMessage(), e);
        }
    }

     */
}