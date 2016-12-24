package com.example.tiger.hw1;

import java.io.Serializable;

public class Person implements Serializable {
    private Long contactId;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String address;
    private String description;

    public Person(Long contactId) {
        this.contactId = contactId;
    }

    public Person(Long contactId, String email, String fullName, String phoneNumber, String address, String description) {
        this.contactId = contactId;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.description = description;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
