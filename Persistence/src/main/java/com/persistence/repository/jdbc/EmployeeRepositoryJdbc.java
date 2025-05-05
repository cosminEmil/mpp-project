package com.persistence.repository.jdbc;

import com.model.Employee;
import com.persistence.EmployeeRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class EmployeeRepositoryJdbc implements EmployeeRepository {
    private final JdbcUtils jdbcUtils;

    public EmployeeRepositoryJdbc(Properties jdbcProps) {
        jdbcUtils = new JdbcUtils(jdbcProps);
    }

    @Override
    public Employee findBy(String email) {
        System.out.println("JDBC findBy email only");
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT name FROM employees WHERE email=?")) {
            preStmt.setString(1, email);
            ResultSet result = preStmt.executeQuery();
            boolean resOk = result.next();
            System.out.println("findBy email " + resOk);
            if (resOk) {
                Employee employee = new Employee(email);
                employee.setName(result.getString("name"));
                return employee;
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public Employee findBy(String name, String email) {
        System.out.println("JDBC findBY 2 params");
        Connection con = jdbcUtils.getConnection();
        try (PreparedStatement preStmt = con.prepareStatement("SELECT name FROM employees WHERE name=? AND email=?")) {
            preStmt.setString(1, name);
            preStmt.setString(2, email);
            ResultSet result = preStmt.executeQuery();
            boolean resOk = result.next();
            System.out.println("findBy name, email " + resOk);
            if (resOk) {
                Employee employee = new Employee(name);
                employee.setName(result.getString("name"));
                return employee;
            }
        } catch (SQLException e) {
            System.out.println("Error DB " + e);
        }
        return null;
    }

    @Override
    public void save(Employee employee) {

    }

    @Override
    public void delete(String s) {

    }

    @Override
    public Employee findOne(String s) {
        return null;
    }

    @Override
    public void update(String s, Employee employee) {

    }

    @Override
    public Iterable<Employee> getAll() {
        return null;
    }
}
