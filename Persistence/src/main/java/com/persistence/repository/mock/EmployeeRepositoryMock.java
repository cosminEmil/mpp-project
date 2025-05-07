package com.persistence.repository.mock;

import com.model.Employee;
import com.persistence.EmployeeRepository;

import java.util.Map;
import java.util.TreeMap;

public class EmployeeRepositoryMock implements EmployeeRepository {
    private final Map<String, Employee> allEmployees;

    public EmployeeRepositoryMock() {
        allEmployees = new TreeMap<>();
        populateEmployees();
    }

    private void populateEmployees() {
        Employee mihai = new Employee("Mihai", "mihai@test.com", "mihai123");
        Employee marius = new Employee("Marius", "marius@test.com", "marius123");
        Employee andrei = new Employee("Andrei", "andrei@test.com", "andrei123");

        allEmployees.put(mihai.getEmail(), mihai);
        allEmployees.put(marius.getEmail(), marius);
        allEmployees.put(andrei.getEmail(), andrei);
    }

    @Override
    public Employee findBy(String email) {
        return allEmployees.get(email);
    }

    @Override
    public Employee findBy(String name, String email) {
        Employee employee = allEmployees.get(email);
        if (employee != null && employee.getName().equals(name)) {
            return employee;
        }
        return null;
    }

    @Override
    public void save(Employee employee) {
        if (employee == null || employee.getEmail() == null) {
            throw new IllegalArgumentException("Employee or email cannot be null");
        }
        allEmployees.put(employee.getEmail(), employee);
    }

    @Override
    public void delete(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        allEmployees.remove(email);
    }

    @Override
    public Employee findOne(String email) {
        return findBy(email);
    }

    @Override
    public void update(String oldEmail, Employee newEmployee) {
        if (oldEmail == null || newEmployee == null) {
            throw new IllegalArgumentException("Arguments cannot be null");
        }

        if (!allEmployees.containsKey(oldEmail)) {
            throw new IllegalArgumentException("Employee not found");
        }

        // Remove old entry if email changed
        if (!oldEmail.equals(newEmployee.getEmail())) {
            allEmployees.remove(oldEmail);
        }

        // Add/update new entry
        allEmployees.put(newEmployee.getEmail(), newEmployee);
    }

    @Override
    public Iterable<Employee> getAll() {
        return allEmployees.values();
    }
}