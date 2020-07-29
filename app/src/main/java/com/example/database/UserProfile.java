package com.example.database;

public class UserProfile {

    private String Email;
    private String Name;
    private String Age;
    private String Gender;

    public UserProfile(){

    }



    public UserProfile(String userEmail, String userName, String userAge, String userGender) {
        this.Email = userEmail;
        this.Name = userName;
        this.Age = userAge;
        this.Gender = userGender;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }
}
