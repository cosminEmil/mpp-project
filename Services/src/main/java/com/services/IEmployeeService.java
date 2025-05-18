package com.services;

import com.model.Employee;

import java.util.List;

public interface IEmployeeService {
    void addObserver(IObserver<Employee, String> observer) throws ServicesException;

    void addEmployee(Employee employee) throws ServicesException;
    void deleteEmployee(Employee employee) throws ServicesException;
    void updateEmployee(String email, Employee employee) throws ServicesException;
    //Employee findEmployeeByEmail(String email) throws ServicesException;
    //Employee findEmployeeByNameAndEmail(String name, String email) throws ServicesException;
    List<Employee> getAllEmployees() throws ServicesException;
    // other specific methods
}
