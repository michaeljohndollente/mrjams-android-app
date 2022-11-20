package com.example.mrjams.ui.login;

public class    User {

    private String email;
    private String avatar;
    private String role;
    private String id;

    public User(String email, String avatar, String role, String id) {
        this.email = email;
        this.avatar = avatar;
        this.role = role;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getRole() {
        return role;
    }

    public String getId() {
        return id;
    }
}
