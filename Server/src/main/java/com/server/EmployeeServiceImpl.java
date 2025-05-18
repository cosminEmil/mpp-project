package com.server;

import com.model.Employee;
import com.persistence.EmployeeRepository;
import com.services.IEmployeeService;
import com.services.IObserver;
import com.services.ServicesException;

import java.util.ArrayList;
import java.util.List;

public class EmployeeServiceImpl implements IEmployeeService, IObserver<Employee, String> {
    private final EmployeeRepository employeeRepository;
    private final List<IObserver<Employee, String>> observers = new ArrayList<>();

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void addObserver(IObserver<Employee, String> observer) {
        observers.add(observer);
    }

    @Override
    public void addEmployee(Employee employee) throws ServicesException {
        try {
            if (employeeRepository.findBy(employee.getEmail()) != null) {
                throw new ServicesException("Employee with email " + employee.getEmail() + " already exists");
            }

            employeeRepository.save(employee);
            notifyAdd(employee);
        } catch (Exception e) {
            throw new ServicesException("Failed to add employee: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteEmployee(Employee employee) throws ServicesException {
        if (employee == null || employee.getEmail() == null) {
            throw new ServicesException("Invalid employee data for deletion.");
        }

        String email = employee.getEmail();
        // Codul real de ștergere în repository
        employeeRepository.delete(employee.getEmail());
        System.out.println("Deleted Employee email: " + email);
        notifyDelete(email);
    }


    @Override
    public void updateEmployee(String email, Employee employee) throws ServicesException {
        try {
            if (employeeRepository.findBy(email) == null) {
                throw new ServicesException("Employee with email " + email + " not found");
            }

            if (!email.equals(employee.getEmail())) {
                if (employeeRepository.findBy(employee.getEmail()) != null) {
                    throw new ServicesException("New email already exists");
                }
            }

            employeeRepository.update(email, employee);
            notifyUpdate(employee, email);
        } catch (Exception e) {
            throw new ServicesException("Failed to update employee: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() throws ServicesException {
        try {
            return (List<Employee>) employeeRepository.getAll(); // presupunem că există această metodă
        } catch (Exception e) {
            throw new ServicesException("Error retrieving employees: " + e.getMessage(), e);
        }    }

    public void notifyAdd(Employee employee) {
        for (IObserver<Employee, String> observer : observers) {
            try {
                observer.notifyAdd(employee);
            } catch (ServicesException e) {
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }

    public void notifyUpdate(Employee employee, String oldEmail) {
        for (IObserver<Employee, String> observer : observers) {
            try {
                observer.notifyUpdate(employee, oldEmail);
            } catch (ServicesException e) {
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }

    public void notifyDelete(String email) {
        for (IObserver<Employee, String> observer : observers) {
            try {
                observer.notifyDelete(email);
            } catch (ServicesException e) {
                System.err.println("Error notifying observer: " + e.getMessage());
            }
        }
    }
}