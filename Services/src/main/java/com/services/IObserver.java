package com.services;

import com.model.Employee;

public interface IObserver<T, ID> {
    void notifyCrudOperation(T entity, String operationType) throws ServicesException;
}
