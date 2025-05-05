package com.model;

public class Employee implements Identifiable<String>{
    private String name, email, password;

    public Employee(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Employee(String name) {

    }

    public Employee(String name, String email) {
        this(name, email, "");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void setID(String id) {
        name = id;
    }

    @Override
    public String getID() {
        return name;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
