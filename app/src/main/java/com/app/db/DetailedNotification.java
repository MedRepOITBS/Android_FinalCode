package com.app.db;

/**
 * Created by Kishore on 9/23/2015.
 */
public class DetailedNotification {

    private int detailId;
    private int notificationId;
    private String detailTitle;
    private String detailDesc;
    private String contentType;
    private int contentSeq;
    private String contentLocation;
    private String contentName;

    /*private String contentLocalPath = "";
    private boolean isRead = false;*/

    public DetailedNotification(int detailId, int notificationId, String detailTitle, String detailDesc, String contentType, int contentSeq, String contentLocation, String contentName, String contentLocalPath) {
        this.detailId = detailId;
        this.notificationId = notificationId;
        this.detailTitle = detailTitle;
        this.detailDesc = detailDesc;
        this.contentType = contentType;
        this.contentSeq = contentSeq;
        this.contentLocation = contentLocation;
        this.contentName = contentName;
       /* this.contentLocalPath = contentLocalPath;
        this.isRead = isRead;*/
    }

    /*public String getContentLocalPath() {
        return contentLocalPath;
    }

    public void setContentLocalPath(String contentLocalPath) {
        this.contentLocalPath = contentLocalPath;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }*/

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

    public static final class COLUMN_NAMES{
        public static final String ID = "ID";
        public static final String NOTIFICATION_ID = "NotificationID";
        public static final String CONTENT_TYPE = "ContentType";
        public static final String CONTENT_SEQ = "ContentSeq";
        public static final String CONTENT_LOC = "ContentLoc";
        public static final String CONTENT_NAME = "ContentName";
        public static final String TITLE = "Title";
        public static final String DESCRIPTION = "Description";
        public static final String IMAGE_CONTENT = "ImageContent";
       /* public static final String IS_READ = "IsRead";
        public static final String IS_FAVOURITE = "IsFavourite";*/
    }

}
