package com.example.database;

public class StudentList {

    private String Name, Email, Phone, Address, Role;

    public StudentList(){
    }
    //This is the constructers that are created
    public StudentList(String studentorteacher, String name, String email, String phone, String postal ) {

        this.Name = name;
        Email = email;
        Phone = phone;
        Address = postal;
        Role = studentorteacher;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String postal) {
        Address = postal;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String studentorTeacher) {
        Role = studentorTeacher;
    }
}
