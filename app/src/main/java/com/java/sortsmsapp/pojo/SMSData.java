package com.java.sortsmsapp.pojo;

public class SMSData {
    private String phone;
    private String message;
    private String Date;

    public SMSData() {
    }

    public SMSData(String phone, String message, String date) {
        this.phone = phone;
        this.message = message;
        Date = date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    @Override
    public String toString() {
        return "SMSData{" +
                "phone='" + phone + '\'' +
                ", message='" + message + '\'' +
                ", Date='" + Date + '\'' +
                '}';
    }
}
