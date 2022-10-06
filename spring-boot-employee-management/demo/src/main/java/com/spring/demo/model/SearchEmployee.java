package com.spring.demo.model;


public class SearchEmployee {

    private String employeeId;
    private String firstName;
    private String lastName;
    private String title;
    private String email;
    private String departmentName;
    private String position;
    private String fullName;

    public SearchEmployee(String employeeId, String firstName, String lastName, String title, String email,
            String departmentName,
            String position) {
        this.employeeId = employeeId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
        this.email = email;
        this.departmentName = departmentName;
        this.position = position;
    }

    public SearchEmployee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "SearchEmployee [employeeId=" + employeeId + ", firstName=" + firstName + ", lastName=" + lastName
                + ", title=" + title + ", email=" + email + ", departmentName=" + departmentName + ", position="
                + position + "]";
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }



   

}
