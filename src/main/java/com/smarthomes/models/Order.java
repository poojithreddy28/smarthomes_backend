package com.smarthomes.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;

    private String orderId;
    private String username;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
    private String city;
    private String state;
    private String postalCode;
    private String cardNumber;
    private String expiry;
    private String cvv;
    private String shippingMethod;
    private String storeLocation;
    private HashMap<String, Cart> products;

    // Constructor
    public Order(String username, String firstName, String lastName, String phone, String email, String address,
                 String city, String state, String postalCode, String cardNumber, String expiry, String cvv,
                 String shippingMethod, String storeLocation, HashMap<String, Cart> products) {
        this.orderId = UUID.randomUUID().toString();  // Generate a unique order ID
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.cardNumber = cardNumber;
        this.expiry = expiry;
        this.cvv = cvv;
        this.shippingMethod = shippingMethod;
        this.storeLocation = storeLocation;
        this.products = products;
    }

    // Getters
    public String getOrderId() {
        return orderId;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getExpiry() {
        return expiry;
    }

    public String getCvv() {
        return cvv;
    }

    public String getShippingMethod() {
        return shippingMethod;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public HashMap<String, Cart> getProducts() {
        return products;
    }

    // Setters if needed (optional, depending on your use case)
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public void setShippingMethod(String shippingMethod) {
        this.shippingMethod = shippingMethod;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public void setProducts(HashMap<String, Cart> products) {
        this.products = products;
    }
}
