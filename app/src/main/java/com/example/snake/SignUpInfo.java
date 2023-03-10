package com.example.snake;

public class SignUpInfo {
    private String email;
    private String password;
    private static String userName;
    private String phoneNumber;
    private String dateOfBirth ;
    private String faculty;
    private String department;

    public SignUpInfo() {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;

        this.faculty=faculty;
        this.department=department;

    }

    public SignUpInfo(String email, String password, String userName, String phoneNumber, String dateOfBirth,String faculty,String department) {
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.faculty=faculty;
        this.department=department;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public  String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getphoneNumber() {
        return phoneNumber;
    }

    public void setphoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }


}
