package com.app.pharma;

import java.util.ArrayList;

/**
 * Created by kishore on 15/10/15.
 */
public class PharmaNotification {
    private int notificationId;
    private String notificationDesc;
    private String notificationName;
    private int typeId;
    private int therapeuticId;
    private int companyId;
    //Date Time format is: YYYYMMDDHHmmSS
    private String updatedOn;
    private String createdOn;
    private String validUpto;
    private String updatedBy;
    private String status;
    private String externalRef;
    private String companyName;
    private String therapeuticName;
    private ArrayList<NotificationDetail> notificationDetails = new ArrayList<>();
    private int totalSentNotification;
    private int totalPendingNotifcation;
    private int totalViewedNotifcation;
    private int totalConvertedToAppointment;
    private String fileList;
    private String therapeuticDropDownValues;

    public PharmaNotification(int id, String name, String description, String companyName,
                              int totSentNotifications,
                              int totPendingNotifications,
                              int totViewedNotificaitons,
                              int totConvertedToAppointment) {
        this.notificationId = id;
        this.notificationName = name;
        this.notificationDesc = description;
        this.companyName = companyName;
        this.totalSentNotification = totSentNotifications;
        this.totalPendingNotifcation = totPendingNotifications;
        this.totalViewedNotifcation = totViewedNotificaitons;
        this.totalConvertedToAppointment = totConvertedToAppointment;
    }




    public static final class COLUMN_NAMES{
        public static final String ID = "ID";
        public static final String NAME = "Name";
        public static final String DESCRIPTION = "Description";
        public static final String TYPE_ID = "TypeID";
        public static final String THERAPEUTIC_ID = "TherapeuticID";
        public static final String COMPANY_ID = "CompanyID";
        public static final String UPDATED_ON = "UpdatedOn";
        public static final String UPDATED_BY = "UpdatedBy";
        public static final String CREATED_ON = "CreatedOn";
        public static final String VALID_UPTO = "ValidUpto";
        public static final String STATUS = "Status";
        public static final String EXTERNAL_REF = "ExternalRef";
        public static final String COMPANY_NAME = "CompanyName";
        public static final String THERAPEUTIC_NAME = "TherapeuticName";
        public static final String TOT_SENT_NOTIFICATION = "TotSentNotifications";
        public static final String TOT_PENDING_NOTIFICATION = "TotPendingNotifications";
        public static final String TOT_VIEWED_NOTIFICATION = "TotViewedNotifications";
        public static final String TOT_CONVERTED_TO_APPOINTMENT = "TotConvertedToAppointment";
        public static final String FILES_LIST = "FilesList";
        public static final String THERAPEUTIC_DROPDOWN_VALUES = "TherapeuticDropdownValues";
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

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getValidUpto() {
        return validUpto;
    }

    public void setValidUpto(String validUpto) {
        this.validUpto = validUpto;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public ArrayList<NotificationDetail> getNotificationDetails() {
        return notificationDetails;
    }

    public void setNotificationDetails(ArrayList<NotificationDetail> notificationDetails) {
        this.notificationDetails = notificationDetails;
    }

    public int getTotalSentNotification() {
        return totalSentNotification;
    }

    public void setTotalSentNotification(int totalSentNotification) {
        this.totalSentNotification = totalSentNotification;
    }

    public int getTotalPendingNotifcation() {
        return totalPendingNotifcation;
    }

    public void setTotalPendingNotifcation(int totalPendingNotifcation) {
        this.totalPendingNotifcation = totalPendingNotifcation;
    }

    public int getTotalViewedNotifcation() {
        return totalViewedNotifcation;
    }

    public void setTotalViewedNotifcation(int totalViewedNotifcation) {
        this.totalViewedNotifcation = totalViewedNotifcation;
    }

    public int getTotalConvertedToAppointment() {
        return totalConvertedToAppointment;
    }

    public void setTotalConvertedToAppointment(int totalConvertedToAppointment) {
        this.totalConvertedToAppointment = totalConvertedToAppointment;
    }

    public String getFileList() {
        return fileList;
    }

    public void setFileList(String fileList) {
        this.fileList = fileList;
    }

    public String getTherapeuticDropDownValues() {
        return therapeuticDropDownValues;
    }

    public void setTherapeuticDropDownValues(String therapeuticDropDownValues) {
        this.therapeuticDropDownValues = therapeuticDropDownValues;
    }

    public static class NotificationDetail {
        private int detailId;
        private int notificationId;
        private String detailTitle;
        private String detailDesc;
        private String contentType;
        private int contentSeq;
        private String contentLocation;
        private String contentName;

        public NotificationDetail(int id, int notificationID, String title, String description) {
            this.detailId = id;
            this.notificationId = notificationID;
            this.detailTitle = title;
            this.detailDesc = description;
        }

        public static final class COLUMN_NAMES{
            public static final String ID = "ID";
            public static final String NOTIFICATION_ID = "NotificationID";
            public static final String TITLE = "Title";
            public static final String DESCRIPTION = "Description";
            public static final String CONTENT_TYPE = "ContentType";
            public static final String CONTENT_SEQ = "ContentSeq";
            public static final String CONTENT_LOCATION = "ContentLocation";
            public static final String CONTENT_NAME = "ContentName";
            public static final String IMAGE_CONTENT = "ImageContent";
        }

        public int getDetailId() {
            return detailId;
        }

        public void setDetailId(int detailId) {
            this.detailId = detailId;
        }

        public int getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(int notificationId) {
            this.notificationId = notificationId;
        }

        public String getDetailTitle() {
            return detailTitle;
        }

        public void setDetailTitle(String detailTitle) {
            this.detailTitle = detailTitle;
        }

        public String getDetailDesc() {
            return detailDesc;
        }

        public void setDetailDesc(String detailDesc) {
            this.detailDesc = detailDesc;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public int getContentSeq() {
            return contentSeq;
        }

        public void setContentSeq(int contentSeq) {
            this.contentSeq = contentSeq;
        }

        public String getContentLocation() {
            return contentLocation;
        }

        public void setContentLocation(String contentLocation) {
            this.contentLocation = contentLocation;
        }

        public String getContentName() {
            return contentName;
        }

        public void setContentName(String contentName) {
            this.contentName = contentName;
        }
    }
}
