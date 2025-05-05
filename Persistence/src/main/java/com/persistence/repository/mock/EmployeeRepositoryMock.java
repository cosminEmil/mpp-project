package com.persistence.repository.mock;

import com.model.Employee;
import com.persistence.EmployeeRepository;

import java.util.Map;
import java.util.TreeMap;

public class EmployeeRepositoryMock implements EmployeeRepository {
    private final Map<String, Employee> allEmployees;

    public EmployeeRepositoryMock() {
        allEmployees = new TreeMap<String, Employee>();
    }

    private void populateEmployees() {
        Employee mihai = new Employee("Mihai", "mihai@test.com", "mihai123");
        Employee  marius = new Employee("Marius", "marius@test.com", "marius123");
        Employee andrei = new Employee("Andrei", "andrei@test.com", "andrei123");

        allEmployees.put(mihai.getId(), mihai);
        allEmployees.put(marius.getId(), marius);
        allEmployees.put(andrei.getId(), andrei);
    }

    @Override
    public Employee findBy(String email) {
        Employee employeeR = allEmployees.get(email);
        if (employeeR == null) {
            return null;
        }
        Employee employee = new Employee(email);
        employee.setName(employeeR.getName());
        return employee;
    }

    @Override
    public Employee findBy(String name, String email) {
        Employee employeeR = allEmployees.get(email);
        if ((employeeR != null) && (employeeR.getEmail().equals(email))) {
            Employee res = new Employee(email);
            res.setName(name);
        }
        return null;
    }

    @Override
    public void save(Employee employee) {

    }

    @Override
    public void delete(String s) {
        if (allEmployees.containsKey(s)) {
            allEmployees.remove(s);
        }

    }

    @Override
    public Employee findOne(String s) {
        return findBy(s);
    }

    @Override
    public void update(String s, Employee employee) {

    }

    @Override
    public Iterable<Employee> getAll() {
        return allEmployees.values();
    }
}
