package com.full_stack.trendsetter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class Address {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

   @Column(name = "first_name")
    private String firstName;

   @Column(name = "last-name")
    private String lastName;

   @Column(name = "street_address")
    private String streetAddress;

   @Column(name = "state")
    private String state;

   @Column(name ="zip_code")
    private String zipCode;

    private String mobile;
   @ManyToOne
    @JoinColumn(name = "user_id")
     @JsonIgnore
      private User user;




   public Address(){

   }
    public Address(Long id, String firstName, String lastName, String streetAddress, String state, String zipCode, User user, String mobile) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetAddress = streetAddress;
        this.state = state;
        this.zipCode = zipCode;
        this.user = user;
        this.mobile = mobile;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


}
