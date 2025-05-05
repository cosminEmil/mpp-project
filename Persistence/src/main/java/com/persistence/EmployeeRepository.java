package com.persistence;

import com.model.Employee;

public interface EmployeeRepository extends ICrudRepository<String, Employee>{
    Employee findBy(String email);

    Employee findBy(String name, String email);
}
