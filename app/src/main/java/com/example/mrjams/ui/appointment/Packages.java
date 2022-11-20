package com.example.mrjams.ui.appointment;

public class Packages {
    String id, name;

    public Packages(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Packages{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
