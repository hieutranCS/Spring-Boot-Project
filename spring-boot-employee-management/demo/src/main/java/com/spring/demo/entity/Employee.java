package com.spring.demo.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "employee",  uniqueConstraints = { @UniqueConstraint(columnNames = "employee_id") })
public class Employee implements Serializable {

    @Id
	@GeneratedValue(strategy =  GenerationType.IDENTITY)
	@Column(name = "employee_id")
	private long employeeId;
	
	@Column(name = "first_name")
	private String firstName;
	
	@Column(name = "last_name")
	private String lastName;
	
	@Column(name = "email")
	private String email;

	@Column(name = "type")
	private String type;

	@Column(name = "phone")
	private String phone;

	@Column(name = "title")
	private String title;

	@Column(name = "birthday")
	private String birthday;

	@Column(name = "date_hire")
	private String dateHire;

	@Column(name = "gender")
	private String gender;
	
	public String getFullName() {
		return firstName + " " + lastName;
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
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getDateHire() {
		return dateHire;
	}

	public void setDateHire(String dateHire) {
		this.dateHire = dateHire;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

    public Employee() {
    }


	public long getEmployeeId() {
		return employeeId;
	}


	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public String toString() {
		return "Employee [birthday=" + birthday + ", dateHire=" + dateHire + ", email=" + email + ", employeeId="
				+ employeeId + ", firstName=" + firstName + ", gender=" + gender + ", lastName=" + lastName + ", phone="
				+ phone + ", title=" + title + ", type=" + type + "]";
	}

	public Employee(long employeeId, String firstName, String lastName, String email, String type, String phone,
			String title, String birthday, String dateHire, String gender) {
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.type = type;
		this.phone = phone;
		this.title = title;
		this.birthday = birthday;
		this.dateHire = dateHire;
		this.gender = gender;
	}	

    
}
