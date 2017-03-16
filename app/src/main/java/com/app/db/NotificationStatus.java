package com.app.db;

/**
 * Created by Kishore on 9/23/2015.
 */
public class NotificationStatus {
    private int id;
    private String status;

    public NotificationStatus(int id, String status) {
        this.id = id;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static final class COLUMN_NAMES{
        public static final String ID = "ID";
        public static final String STATUS = "Status";
    }
}
