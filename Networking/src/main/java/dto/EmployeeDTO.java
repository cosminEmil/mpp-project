package dto;

import java.io.Serializable;

public class EmployeeDTO implements Serializable {
    private String name, email, password;

    public EmployeeDTO(String name) {
        this.name = name;
    }

    public EmployeeDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
    public EmployeeDTO(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public EmployeeDTO() {

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "EmployeeDTO{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
