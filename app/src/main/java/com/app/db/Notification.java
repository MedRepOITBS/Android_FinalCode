package com.app.db;

import java.util.ArrayList;

/**
 * Created by Kishore on 9/23/2015.
 */
public class Notification {

    private int notificationId;
    private String notificationDesc;
    private String notificationName;
    private int typeId;
    private int therapeuticId;
    private int companyId;
    private String updatedOn;
    private String updatedBy;
    private String createdOn;
    private String createdBy;
    private String validUpto;
    private String status;
    private String externalRef;
    private String companyName;
    private String therapeuticName;
    private ArrayList<DetailedNotification> notificationDetails;
    private String fileList;

    public boolean isReminderAdded() {
        return isReminderAdded;
    }

    public void setIsReminderAdded(boolean isReminderAdded) {
        this.isReminderAdded = isReminderAdded;
    }

    private boolean isReminderAdded = false;

    private boolean isFavourite = false;
    private boolean isRead = false;

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(boolean isFavourite) {
        this.isFavourite = isFavourite;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getNotificationDesc() {
        return notificationDesc;
    }

    public void setNotificationDesc(String notificationDesc) {
        this.notificationDesc = notificationDesc;
    }

    public String getNotificationName() {
        return notificationName;
    }

    public void setNotificationName(String notificationName) {
        this.notificationName = notificationName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getTherapeuticId() {
        return therapeuticId;
    }

    public void setTherapeuticId(int therapeuticId) {
        this.therapeuticId = therapeuticId;
    }

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(String validUpto) {
        this.validUpto = validUpto;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getTherapeuticName() {
        return therapeuticName;
    }

    public void setTherapeuticName(String therapeuticName) {
        this.therapeuticName = therapeuticName;
    }

    public ArrayList<DetailedNotification> getNotificationDetails() {
        return notificationDetails;
    }

    public void setNotificationDetails(ArrayList<DetailedNotification> notificationDetails) {
        this.notificationDetails = notificationDetails;
    }

    public String getFileList() {
        return fileList;
    }

    public void setFileList(String fileList) {
        this.fileList = fileList;
    }

    public static final class COLUMN_NAMES{
        public static final String ID = "ID";
        public static final String TYPE_ID = "TypeID";
        public static final String THERAPEUTIC_ID = "TherapeuticID";
        public static final String COMPANY_ID = "CompanyID";
        public static final String NAME = "Name";
        public static final String DESCRIPTION = "Description";
        public static final String CREATED_ON = "CreatedOn";
        public static final String CREATED_BY = "CreatedBy";
        public static final String UPDATED_ON = "UpdatedOn";
        public static final String UPDATED_BY = "UpdatedBy";
        public static final String VALID_UPTO = "ValidUpto";
        public static final String STATUS = "Status";
        public static final String EXT_REF = "ExtRef";
        public static final String LOGO_URL = "LogoURL";
        public static final String LOGO_PATH = "LogoPath";
        public static final String IS_FAVOURITE = "IsFavourite";
        public static final String IS_FEEDBACK_GIVEN = "IsFeedbackGiven";
        public static final String IS_REMINDER_ADDED = "IsReminderAdded";
        public static final String IS_READ = "IsRead";
    }
}
