package com.brangelov.junitapp.entities;

public class Reservation extends Entity {

    public Reservation() {
    }

    public Reservation(String firstName, String lastName, String phoneNumber, String viewingId, int row, int seat) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.viewingId = viewingId;
        this.row = row;
        this.seat = seat;
    }

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String viewingId;

    private int row;

    private int seat;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getViewingId() {
        return viewingId;
    }

    public void setViewingId(String viewingId) {
        this.viewingId = viewingId;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }
}
