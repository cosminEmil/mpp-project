package com.model;

public class Ticket implements Identifiable<Integer>{
    private int id, noSeats;
    private double price;
    private String customerName, tourists, customerAddress;

    public Ticket(double price, int noSeats, String customerName, String customerAddress, String tourists) {
        this.price = price;
        this.noSeats = noSeats;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.tourists = tourists;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNoSeats() {
        return noSeats;
    }

    public void setNoSeats(int noSeats) {
        this.noSeats = noSeats;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTourists() {
        return tourists;
    }

    public void setTourists(String tourists) {
        this.tourists = tourists;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
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
        return "Ticket{" +
                "id=" + id +
                ", noSeats=" + noSeats +
                ", price=" + price +
                ", customerName='" + customerName + '\'' +
                ", tourists='" + tourists + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                '}';
    }
}
