package com.model;

public class Flight implements Identifiable<Integer>{
    private int id, noAvailableSeats;
    private String destination, departure, airport;

    public Flight(int noAvailableSeats, String destination, String departure, String airport) {
        this.noAvailableSeats = noAvailableSeats;
        this.destination = destination;
        this.departure = departure;
        this.airport = airport;
    }

    public int getNoAvailableSeats() {
        return noAvailableSeats;
    }

    public void setNoAvailableSeats(int noAvailableSeats) {
        this.noAvailableSeats = noAvailableSeats;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public String toString() {
        return "Flight{" +
                "airport='" + airport + '\'' +
                ", noAvailableSeats=" + noAvailableSeats +
                ", departure='" + departure + '\'' +
                ", destination='" + destination + '\'' +
                '}';
    }
}
