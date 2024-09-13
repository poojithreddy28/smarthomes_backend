
package com.smarthomes.models;

import java.io.Serializable;

public class NewUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private String fullName;
    private String email;
    private String password;

    public NewUser(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
