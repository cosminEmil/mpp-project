package jsonprotocol;

import dto.EmployeeDTO;

public class Request {
    //TODO: DO THE REQUEST CLASS

    private RequestType type;
    private EmployeeDTO employee;

    public Request() {}

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", employee=" + employee +
                '}';
    }
}
