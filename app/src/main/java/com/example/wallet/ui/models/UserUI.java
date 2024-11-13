package com.example.wallet.ui.models;

public class UserUI {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;

    public UserUI(String name, String lasName, String email, String password, String confirmPassword) {
        this.name = name;
        this.lastName = lasName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public boolean isPasswordEquals(){
        return this.password.equals(this.confirmPassword);
    }
    public boolean isValid() {
        return !this.name.isEmpty() &&
                !this.lastName.isEmpty() &&
                !this.email.isEmpty() &&
                !this.password.isEmpty() &&
                !this.confirmPassword.isEmpty() &&
                isPasswordEquals();
    }
}
