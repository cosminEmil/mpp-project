package jsonprotocol;

import com.model.Employee;
import dto.EmployeeDTO;

public class Response {
    //TODO: DO THE RESPONSE CLASS
    private ResponseType type;
    private String errorMessage;
    private EmployeeDTO employee;

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

    @Override
    public String toString() {
        return "Response{" +
                "type=" + type +
                ", errorMessage='" + errorMessage + '\'' +
                ", employee=" + employee +
                '}';
    }
}
