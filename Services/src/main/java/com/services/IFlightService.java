package com.services;

import com.model.Flight;

import java.util.List;

public interface IFlightService {
    void addFlight(Flight flight) throws ServicesException;
    void cancelFlight(String flightName) throws ServicesException;
    void updateFlight(String flightName, Flight flight) throws ServicesException;
    Flight findFlightbyName(String flightName) throws ServicesException;
    // List<String> searchFlights(String destination) throws ServicesException;
    List<String> getAllFlights() throws ServicesException;
    // other specific methods
}
