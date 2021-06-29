package com.example.jwork_android;

public class Recruiter {
    private int id;
    private String name, email, phoneNumber;
    private Location location;

    public Recruiter(int id,String name, String email, String phoneNumber, Location location)
    {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.location = location;
    }

    public int getId()
    {
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getphoneNumber(){
        return this.phoneNumber;
    }

    public Location getLocation(){
        return this.location;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setphoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }

    public void setLocation(Location location){
        this.location = location;
    }
}