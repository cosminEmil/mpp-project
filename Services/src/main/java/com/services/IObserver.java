package com.services;

import com.model.Employee;

public interface IObserver<T, ID> {
    void notifyAdd(T entity) throws ServicesException;
    void notifyUpdate(T entity, String ID) throws ServicesException;
    void notifyDelete(String ID) throws ServicesException;
    void addObserver(IObserver<Employee, String> observer) throws ServicesException;

}
