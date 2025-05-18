package jsonprotocol;

import com.model.Employee;
import dto.EmployeeDTO;

import java.util.List;

public class Response {
    //TODO: DO THE RESPONSE CLASS
    private ResponseType type;
    private String errorMessage;
    private EmployeeDTO employee;
    private List<EmployeeDTO> employees;

    public  Response() {}

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public List<EmployeeDTO> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeDTO> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", employee=" + employee +
                '}';
    }

    public void setEmployeeEmail(String email) {
        if (employee == null) {
            employee = new EmployeeDTO();
        }
        employee.setEmail(email);  // Setează doar email-ul în EmployeeDTO
    }


    public String getEmployeeEmail() {
        if (employee != null) {
            return employee.getEmail();  // Presupunem că EmployeeDTO are o metodă getEmail()
        }
        return null;  // Dacă employee este null, returnăm null
    }
}
