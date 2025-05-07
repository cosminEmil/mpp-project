package com.persistence.repository.jdbc;

import com.model.Employee;
import com.persistence.EmployeeRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmployeeRepositoryJdbc implements EmployeeRepository {
    private final JdbcUtils jdbcUtils;

    public EmployeeRepositoryJdbc(Properties jdbcProps) {
        this.jdbcUtils = new JdbcUtils(jdbcProps);
        initializeDatabase();
    }

    private void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS employees (\n"
                + "    name TEXT NOT NULL,\n"
                + "    email TEXT PRIMARY KEY,\n"
                + "    password TEXT NOT NULL\n"
                + ");";

        try (Connection conn = jdbcUtils.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Database tables initialized");
        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    @Override
    public Employee findBy(String email) {
        String sql = "SELECT name, password FROM employees WHERE email = ?";
        try (Connection conn = jdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Employee(
                        rs.getString("name"),
                        email,
                        rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error finding employee by email: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Employee findBy(String name, String email) {
        String sql = "SELECT password FROM employees WHERE name = ? AND email = ?";
        try (Connection conn = jdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Employee(name, email, rs.getString("password"));
            }
        } catch (SQLException e) {
            System.err.println("Error finding employee by name and email: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void save(Employee employee) {
        String sql = "INSERT INTO employees(name, email, password) VALUES (?, ?, ?)";
        try (Connection conn = jdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPassword());
            stmt.executeUpdate();
            System.out.println("Employee saved: " + employee.getEmail());
        } catch (SQLException e) {
            System.err.println("Error saving employee: " + e.getMessage());
            throw new RuntimeException("Failed to save employee", e);
        }
    }

    @Override
    public void delete(String email) {
        String sql = "DELETE FROM employees WHERE email = ?";
        try (Connection conn = jdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            int affectedRows = stmt.executeUpdate();
            System.out.println("Deleted " + affectedRows + " employee(s) with email: " + email);
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            throw new RuntimeException("Failed to delete employee", e);
        }
    }

    @Override
    public Employee findOne(String email) {
        return findBy(email);
    }

    @Override
    public void update(String oldEmail, Employee newEmployee) {
        String sql = "UPDATE employees SET name = ?, email = ?, password = ? WHERE email = ?";
        try (Connection conn = jdbcUtils.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newEmployee.getName());
            stmt.setString(2, newEmployee.getEmail());
            stmt.setString(3, newEmployee.getPassword());
            stmt.setString(4, oldEmail);
            stmt.executeUpdate();
            System.out.println("Employee updated from " + oldEmail + " to " + newEmployee.getEmail());
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            throw new RuntimeException("Failed to update employee", e);
        }
    }

    @Override
    public Iterable<Employee> getAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT name, email, password FROM employees";
        try (Connection conn = jdbcUtils.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                employees.add(new Employee(
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error getting all employees: " + e.getMessage());
            throw new RuntimeException("Failed to get employees", e);
        }
        return employees;
    }
}