package com.example.asohail4.bluetoothserver.Models;

public class MyDevice {
    private String Name;
    private String Address;


    public MyDevice(){

    }

    public MyDevice(String name, String address) {
        Name = name;
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
