package com.app.db;

/**
 * Created by Kishore on 9/23/2015.
 */
public class NotificationType {
    private int typeId;
    private String typeName;
    private String typeDesc;


    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public static final class COLUMN_NAMES{
        public static final String ID = "ID";
        public static final String NAME = "Name";
        public static final String DESCRIPTION = "Description";
    }

}
