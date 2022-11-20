package com.example.mrjams.ui.clinic;

public class ClinicList {

    private String id, name, phone, telephone, type_of_clinic, address_line_1, address_line_2, packages, services;



    public ClinicList(String id, String name, String phone, String telephone, String type_of_clinic, String address_line_1, String address_line_2, String packages, String services) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.telephone = telephone;
        this.type_of_clinic = type_of_clinic;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.packages = packages;
        this.services = services;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getType_of_clinic() {
        return type_of_clinic;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public String getPackages() {
        return packages;
    }

    public String getServices() {
        return services;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setType_of_clinic(String type_of_clinic) {
        this.type_of_clinic = type_of_clinic;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public void setServices(String services) {
        this.services = services;
    }
}
