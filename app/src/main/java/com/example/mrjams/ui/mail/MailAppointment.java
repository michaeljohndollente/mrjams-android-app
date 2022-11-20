package com.example.mrjams.ui.mail;

public class MailAppointment {

    private String date_created, appointment_date, remark, name, id;


    public MailAppointment(String id, String name, String remark, String appointment_date,
                              String date_created) {
        this.id = id;
        this.name = name;
        this.remark = remark;
        this.appointment_date = appointment_date;
        this.date_created = date_created;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRemark() {
        return remark;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public String getDate_created() {
        return date_created;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }
}
