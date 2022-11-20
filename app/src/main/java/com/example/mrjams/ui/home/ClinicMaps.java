package com.example.mrjams.ui.home;

public class ClinicMaps {
    String id, name, phone, telephone, type, address_line_1, address_line_2, longi, lati, city, zipcode;

    public ClinicMaps(String id, String name, String phone, String telephone, String type, String address_line_1, String address_line_2, String longi, String lati, String city, String zipcode) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.telephone = telephone;
        this.type = type;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.longi = longi;
        this.lati = lati;
        this.city = city;
        this.zipcode = zipcode;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public String toString() {
        return "ClinicMaps{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", telephone='" + telephone + '\'' +
                ", type='" + type + '\'' +
                ", address_line_1='" + address_line_1 + '\'' +
                ", address_line_2='" + address_line_2 + '\'' +
                ", longi='" + longi + '\'' +
                ", lati='" + lati + '\'' +
                ", city='" + city + '\'' +
                ", zipcode='" + zipcode + '\'' +
                '}';
    }
}
