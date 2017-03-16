package com.app.db;

/**
 * Created by GunaSekhar on 09-06-2016.
 */
public class DoctorContacts {

    public static final String CONTACTID = "contactId";
    public static final String CONTACTNAME = "contactName";
    public static final String CONTACTIMAGE = "contactImage";
    public static final String CONTACTSPECILIST = "contactSpecialist";

    private int id;
    private String name;
    private String specialist;

    //Constructor
    public DoctorContacts() {

    }

    //Constructor
    public DoctorContacts(int id, String name, String specialist) {
        this.id = id;
        this.name = name;
        this.specialist = specialist;
    }

    //Constructor
    public DoctorContacts(String name, String specialist) {
        this.name = name;
        this.specialist = specialist;
    }

    //setId
    public void setId(int id) {
        this.id = id;
    }

    //getId
    public int getId() {
        return this.id;
    }

    //setContactName
    public void setName(String name) {
        this.name = name;
    }

    //getContactName
    public String getName() {
        return this.name;
    }

    //setContactSpecialist
    public void setContactSpecialist(String specialist) {
        this.specialist = specialist;
    }

    //getContactSpecialist
    public String getContactspecialist() {
        return this.specialist;
    }
}
