package dto;

import com.model.Employee;

public class DTOUtils {
    public static Employee getFromDTO(EmployeeDTO employeeDTO) {
        String name = employeeDTO.getName();
        String email = employeeDTO.getEmail();
        String password = employeeDTO.getPassword();
        return new Employee(name,email, password);
    }

    public static EmployeeDTO getDTO(Employee employee) {
        String name = employee.getName();
        String email = employee.getEmail();
        String password = employee.getPassword();
        return new EmployeeDTO(name, email, employee.getPassword());
    }
}
