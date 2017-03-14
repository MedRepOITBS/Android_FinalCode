package com.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import com.app.pharma.PharmaNotification;
import com.app.util.Utils;

import java.util.ArrayList;
import java.util.List;

import pharma.PharmaCampainDetails;

/**
 * Created by Kishore on 9/23/2015.
 */
public class MedRepDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "MedRepDB";

    // Table names
    private static final String TABLE_COMPANY = "Company";
    private static final String TABLE_DISPLAY_PICTURE = "DisplayPicture";
    private static final String TABLE_LOCATION = "Location";


    private static final String TABLE_THERAPEUTIC_CATEGORIES = "TherapeuticCategories";
    //Describes whether notification is new or not
    private static final String TABLE_NOTIFICATION_STATUS = "NotificationStatus";
    private static final String TABLE_NOTIFICATION_TYPE = "NotificationType";
    private static final String TABLE_NOTIFICATIONS = "Notifications";
    private static final String TABLE_DETAILED_NOTIFICATION = "DetailedNotification";
    private static final String TABLE_PHARMA_NOTIFICATIONS = "PharmaNotifications";
    private static final String TABLE_PHARMA_DETAILED_NOTIFICATIONS = "PharmaDetailedNotifications";
    private static final String TABLE_DOCTOR_CONTACTS = "DoctorContacts";

//    private static final String TABLE_

    private static MedRepDatabaseHandler THIS_INSTANCE = null;

    public MedRepDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MedRepDatabaseHandler getInstance(){
        return THIS_INSTANCE;
    }

    public static MedRepDatabaseHandler getInstance(Context context){
        if(THIS_INSTANCE == null){
            THIS_INSTANCE = new MedRepDatabaseHandler(context);
        }
        return THIS_INSTANCE;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        //Create display picture table
        String createDisplayPictureTable = "CREATE TABLE " + TABLE_DISPLAY_PICTURE + " ("
                + Company.DisplayPicture.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + Company.DisplayPicture.COLUMN_NAMES.DATA + " TEXT, "
                + Company.DisplayPicture.COLUMN_NAMES.MIME_TYPE + " TEXT, "
                + Company.DisplayPicture.COLUMN_NAMES.LOGIN_ID + " INTEGER, "
                + Company.DisplayPicture.COLUMN_NAMES.SECURITY_ID + " INTEGER)";

        //Create location table
        String createLocationTable = "CREATE TABLE " + TABLE_LOCATION + " ("
                + Company.Location.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + Company.Location.COLUMN_NAMES.ADDRESS1 + " TEXT, "
                + Company.Location.COLUMN_NAMES.ADDRESS2 + " TEXT, "
                + Company.Location.COLUMN_NAMES.CITY + " TEXT, "
                + Company.Location.COLUMN_NAMES.STATE + " TEXT, "
                + Company.Location.COLUMN_NAMES.COUNTRY + " TEXT, "
                + Company.Location.COLUMN_NAMES.ZIP_CODE + " TEXT, "
                + Company.Location.COLUMN_NAMES.TYPE + " TEXT)";

        //Create company table statement
        String createCompanyTable = "CREATE TABLE " + TABLE_COMPANY + " ("
                + Company.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + Company.COLUMN_NAMES.NAME + " TEXT, "
                + Company.COLUMN_NAMES.DESCRIPTION + " TEXT, "
                + Company.COLUMN_NAMES.CONTACT_NAME + " TEXT, "
                + Company.COLUMN_NAMES.CONTACT_NUMBER + " TEXT, "
                + Company.COLUMN_NAMES.COMPANY_URL + " TEXT, "
                + Company.COLUMN_NAMES.STATUS + " TEXT, "
                + Company.COLUMN_NAMES.THERAPEUTIC_AREAS + " TEXT, "
                + Company.COLUMN_NAMES.DP_ID + " INTEGER, "
                + Company.COLUMN_NAMES.LOCATION_ID + " INTEGER, "
                + "FOREIGN KEY (" + Company.COLUMN_NAMES.DP_ID +") REFERENCES " + TABLE_DISPLAY_PICTURE + "(" + Company.DisplayPicture.COLUMN_NAMES.ID +")"
                + "FOREIGN KEY (" + Company.COLUMN_NAMES.LOCATION_ID +") REFERENCES " + TABLE_LOCATION + "(" + Company.Location.COLUMN_NAMES.ID +")"
                + ")";

        //Create TherapeuticCategories table statement
        String createTherapeuticCategoriesTable = "CREATE TABLE " + TABLE_THERAPEUTIC_CATEGORIES + " ("
                + TherapeuticCategory.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + TherapeuticCategory.COLUMN_NAMES.NAME + " TEXT, "
                + TherapeuticCategory.COLUMN_NAMES.DESCRIPTION + " TEXT, "
                + TherapeuticCategory.COLUMN_NAMES.COMPANIES + " TEXT, "
                + TherapeuticCategory.COLUMN_NAMES.COMPANY_ID + " INTEGER)";

        //Create NotificationStatus table statement
        String createNotificationStatusTable = "CREATE TABLE " + TABLE_NOTIFICATION_STATUS + " ("
                + NotificationStatus.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + NotificationStatus.COLUMN_NAMES.STATUS + " TEXT)";

        //Create NotificationType table statement
        String createNotificationTypeTable = "CREATE TABLE " + TABLE_NOTIFICATION_TYPE + " ("
                + NotificationType.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + NotificationType.COLUMN_NAMES.NAME + " TEXT, "
                + NotificationType.COLUMN_NAMES.DESCRIPTION + " TEXT)";


        //Create Notifications table statement
        String createNotificationsTable = "CREATE TABLE " + TABLE_NOTIFICATIONS + " ("
                + Notification.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + Notification.COLUMN_NAMES.THERAPEUTIC_ID + " INTEGER, "
                + Notification.COLUMN_NAMES.COMPANY_ID + " INTEGER, "
                + Notification.COLUMN_NAMES.NAME + " TEXT, "
                + Notification.COLUMN_NAMES.DESCRIPTION + " TEXT, "
                + Notification.COLUMN_NAMES.CREATED_ON + " TEXT, "
                + Notification.COLUMN_NAMES.CREATED_BY + " TEXT, "
                + Notification.COLUMN_NAMES.UPDATED_ON + " TEXT, "
                + Notification.COLUMN_NAMES.UPDATED_BY + " TEXT, "
                + Notification.COLUMN_NAMES.VALID_UPTO + " TEXT, "
                + Notification.COLUMN_NAMES.STATUS + " TEXT, "
                + Notification.COLUMN_NAMES.EXT_REF + " TEXT, "
                + Notification.COLUMN_NAMES.LOGO_URL + " TEXT, "
                + Notification.COLUMN_NAMES.LOGO_PATH + " TEXT, "
                + Notification.COLUMN_NAMES.IS_READ + " BOOLEAN DEFAULT 0, "
                + Notification.COLUMN_NAMES.IS_REMINDER_ADDED + " BOOLEAN DEFAULT 0, "
                + Notification.COLUMN_NAMES.IS_FAVOURITE + " BOOLEAN DEFAULT 0, "
                + Notification.COLUMN_NAMES.TYPE_ID + " INTEGER, "
                + Notification.COLUMN_NAMES.IS_FEEDBACK_GIVEN + " BOOLEAN DEFAULT 0, " +
                "FOREIGN KEY (" + Notification.COLUMN_NAMES.TYPE_ID +") REFERENCES " + TABLE_NOTIFICATION_TYPE + "(" + NotificationType.COLUMN_NAMES.ID +"), " +
                "FOREIGN KEY (" + Notification.COLUMN_NAMES.THERAPEUTIC_ID +") REFERENCES " + TABLE_THERAPEUTIC_CATEGORIES + "(" + TherapeuticCategory.COLUMN_NAMES.ID +"), " +
                "FOREIGN KEY (" + Notification.COLUMN_NAMES.COMPANY_ID +") REFERENCES " + TABLE_COMPANY + "(" + Company.COLUMN_NAMES.ID +")" +
                ")";

        //Create DetailedNotification table statement
        String createDetailedNotification = "CREATE TABLE " + TABLE_DETAILED_NOTIFICATION + " ("
                + DetailedNotification.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + DetailedNotification.COLUMN_NAMES.NOTIFICATION_ID + " INTEGER, "
                + DetailedNotification.COLUMN_NAMES.CONTENT_TYPE + " TEXT, "
                + DetailedNotification.COLUMN_NAMES.CONTENT_SEQ + " INTEGER, "
                + DetailedNotification.COLUMN_NAMES.CONTENT_LOC + " TEXT, "
                + DetailedNotification.COLUMN_NAMES.CONTENT_NAME + " TEXT, "
                + DetailedNotification.COLUMN_NAMES.TITLE + " TEXT, "
                + DetailedNotification.COLUMN_NAMES.DESCRIPTION + " TEXT, "
                + DetailedNotification.COLUMN_NAMES.IMAGE_CONTENT + " TEXT, "
                + "FOREIGN KEY (" + DetailedNotification.COLUMN_NAMES.NOTIFICATION_ID +") REFERENCES " + TABLE_NOTIFICATIONS + "(" + Notification.COLUMN_NAMES.ID +"))";

        String createPharmaNotifications = "CREATE TABLE " + TABLE_PHARMA_NOTIFICATIONS + " (" +
                PharmaNotification.COLUMN_NAMES.ID + " INTEGER PRIMARY_KEY, " +
                PharmaNotification.COLUMN_NAMES.NAME + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.DESCRIPTION + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.TYPE_ID + " INTEGER, " +
                PharmaNotification.COLUMN_NAMES.THERAPEUTIC_ID + " INTEGER, " +
                PharmaNotification.COLUMN_NAMES.COMPANY_ID + " INTEGER, " +
                PharmaNotification.COLUMN_NAMES.UPDATED_ON + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.UPDATED_BY + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.CREATED_ON + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.VALID_UPTO + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.STATUS + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.EXTERNAL_REF + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.COMPANY_NAME + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.THERAPEUTIC_NAME + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.TOT_SENT_NOTIFICATION + " INTEGER, " +
                PharmaNotification.COLUMN_NAMES.TOT_PENDING_NOTIFICATION + " INTEGER, " +
                PharmaNotification.COLUMN_NAMES.TOT_VIEWED_NOTIFICATION + " INTEGER, " +
                PharmaNotification.COLUMN_NAMES.TOT_CONVERTED_TO_APPOINTMENT + " INTEGER, " +
                PharmaNotification.COLUMN_NAMES.FILES_LIST + " TEXT, " +
                PharmaNotification.COLUMN_NAMES.THERAPEUTIC_DROPDOWN_VALUES + " TEXT)";

        //Create DetailedNotification table statement
        String createPharmaDetailedNotification = "CREATE TABLE " + TABLE_PHARMA_DETAILED_NOTIFICATIONS + " ("
                + PharmaNotification.NotificationDetail.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + PharmaNotification.NotificationDetail.COLUMN_NAMES.NOTIFICATION_ID + " INTEGER, "
                + PharmaNotification.NotificationDetail.COLUMN_NAMES.CONTENT_TYPE + " TEXT, "
                + PharmaNotification.NotificationDetail.COLUMN_NAMES.CONTENT_SEQ + " INTEGER, "
                + PharmaNotification.NotificationDetail.COLUMN_NAMES.CONTENT_LOCATION + " TEXT, "
                + PharmaNotification.NotificationDetail.COLUMN_NAMES.CONTENT_NAME + " TEXT, "
                + PharmaNotification.NotificationDetail.COLUMN_NAMES.TITLE + " TEXT, "
                + PharmaNotification.NotificationDetail.COLUMN_NAMES.DESCRIPTION + " TEXT, "
                + PharmaNotification.NotificationDetail.COLUMN_NAMES.IMAGE_CONTENT + " TEXT, "
                + "FOREIGN KEY (" + PharmaNotification.NotificationDetail.COLUMN_NAMES.NOTIFICATION_ID +
                    ") REFERENCES " + TABLE_PHARMA_NOTIFICATIONS + "(" + PharmaNotification.COLUMN_NAMES.ID +"))";

        String createDoctorContacts = "CREATE TABLE " + TABLE_DOCTOR_CONTACTS + "( " + DoctorContacts.CONTACTID + " INTEGER PRIMARY KEY, "+ DoctorContacts.CONTACTNAME +" TEXT, " +
                DoctorContacts.CONTACTSPECILIST +" TEXT)";



        db.execSQL(createDisplayPictureTable);
        db.execSQL(createLocationTable);
        db.execSQL(createCompanyTable);
        db.execSQL(createTherapeuticCategoriesTable);
        db.execSQL(createNotificationStatusTable);
        db.execSQL(createNotificationTypeTable);
        db.execSQL(createNotificationsTable);
        db.execSQL(createDetailedNotification);
        db.execSQL(createPharmaNotifications);
        db.execSQL(createPharmaDetailedNotification);
        db.execSQL(createDoctorContacts);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DISPLAY_PICTURE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMPANY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_THERAPEUTIC_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_STATUS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATION_TYPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DETAILED_NOTIFICATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHARMA_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHARMA_DETAILED_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOCTOR_CONTACTS);
        onCreate(db);
    }

    public ArrayList<DoctorContacts> getDoctorContactDetails(){
        ArrayList<DoctorContacts> contactDetails = new ArrayList<>();
        String selectQuery = "SELECT * FROM "+TABLE_DOCTOR_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                DoctorContacts contact = new DoctorContacts();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setName(cursor.getString(1));
                contact.setContactSpecialist(cursor.getString(2));
                // Adding contact to list
                contactDetails.add(contact);
            } while (cursor.moveToNext());
        }
        return contactDetails;
    }

    public int getPharmaNotificationsLatestUpdatedDate(){

        long lastUpdated = Utils.DEFAULT_START_DATE;

        String projection[] = {PharmaNotification.COLUMN_NAMES.UPDATED_ON};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHARMA_NOTIFICATIONS, projection, null, null, null, null, PharmaNotification.COLUMN_NAMES.UPDATED_ON);

        Log.i("MedRep", cursor.getCount() + "");

        if(cursor.getCount() > 0 && cursor.moveToFirst()){

            do{
                long temp = Long.parseLong(cursor.getString(cursor.getColumnIndex(PharmaNotification.COLUMN_NAMES.UPDATED_ON)));

                if(temp > lastUpdated){
                    lastUpdated = temp;
                }
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        if(lastUpdated != -1){
            String str = lastUpdated + "";
            return Integer.parseInt(str.substring(0, 8)) + 1;
        }

        return (int)lastUpdated;
    }

    public ArrayList<PharmaNotification> getPharmaNotifications(){
        ArrayList<PharmaNotification> pharmaNotifications = null;

        String projection[] = {PharmaNotification.COLUMN_NAMES.ID,
                PharmaNotification.COLUMN_NAMES.NAME,
                PharmaNotification.COLUMN_NAMES.DESCRIPTION,
                PharmaNotification.COLUMN_NAMES.COMPANY_NAME,
                PharmaNotification.COLUMN_NAMES.TOT_SENT_NOTIFICATION,
                PharmaNotification.COLUMN_NAMES.TOT_PENDING_NOTIFICATION,
                PharmaNotification.COLUMN_NAMES.TOT_VIEWED_NOTIFICATION,
                PharmaNotification.COLUMN_NAMES.TOT_CONVERTED_TO_APPOINTMENT
        };

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHARMA_NOTIFICATIONS, projection, null, null, null, null, PharmaNotification.COLUMN_NAMES.UPDATED_ON);

        Log.i("MedRep", cursor.getCount() + "");

        if(cursor.getCount() > 0 && cursor.moveToFirst()){

            pharmaNotifications = new ArrayList<>();

            do{
                int id = cursor.getInt(cursor.getColumnIndex(PharmaNotification.COLUMN_NAMES.ID));
                String name = cursor.getString(cursor.getColumnIndex(PharmaNotification.COLUMN_NAMES.NAME));
                String description = cursor.getString(cursor.getColumnIndex(PharmaNotification.COLUMN_NAMES.DESCRIPTION));
                String companyName = cursor.getString(cursor.getColumnIndex(PharmaNotification.COLUMN_NAMES.COMPANY_NAME));
                int totSentNotifications = cursor.getInt(cursor.getColumnIndex(PharmaNotification.COLUMN_NAMES.TOT_SENT_NOTIFICATION));
                int totPendingNotifications = cursor.getInt(cursor.getColumnIndex(PharmaNotification.COLUMN_NAMES.TOT_PENDING_NOTIFICATION));
                int totViewedNotificaitons = cursor.getInt(cursor.getColumnIndex(PharmaNotification.COLUMN_NAMES.TOT_VIEWED_NOTIFICATION));
                int totConvertedToAppointment = cursor.getInt(cursor.getColumnIndex(PharmaNotification.COLUMN_NAMES.TOT_CONVERTED_TO_APPOINTMENT));

                PharmaNotification pharmaNotification =
                        new PharmaNotification(
                                id,
                                name,
                                description,
                                companyName,
                                totSentNotifications,
                                totPendingNotifications,
                                totViewedNotificaitons,
                                totConvertedToAppointment);

                pharmaNotifications.add(pharmaNotification);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return pharmaNotifications;
    }

    public ArrayList<PharmaNotification.NotificationDetail> getPharmaNotificationDetails(int notificationID){
        ArrayList<PharmaNotification.NotificationDetail> notificationDetails = null;

        String where = PharmaNotification.NotificationDetail.COLUMN_NAMES.NOTIFICATION_ID + " = " + notificationID;

        String projection[] = {PharmaNotification.NotificationDetail.COLUMN_NAMES.ID,
                PharmaNotification.NotificationDetail.COLUMN_NAMES.TITLE,
                PharmaNotification.NotificationDetail.COLUMN_NAMES.DESCRIPTION};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHARMA_DETAILED_NOTIFICATIONS,
                projection,
                where,
                null, null, null,
                PharmaNotification.NotificationDetail.COLUMN_NAMES.ID);

        Log.i("MedRep", cursor.getCount() + "");

        if(cursor.getCount() > 0 && cursor.moveToFirst()){

            notificationDetails = new ArrayList<>();

            do{
                int id = cursor.getInt(cursor.getColumnIndex(PharmaNotification.NotificationDetail.COLUMN_NAMES.ID));
                String title = cursor.getString(cursor.getColumnIndex(PharmaNotification.NotificationDetail.COLUMN_NAMES.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(PharmaNotification.NotificationDetail.COLUMN_NAMES.DESCRIPTION));

                PharmaNotification.NotificationDetail notificationDetail =
                        new PharmaNotification.NotificationDetail(id, notificationID, title, description);

                notificationDetails.add(notificationDetail);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return notificationDetails;
    }


    public void addPharmaNotifications(ArrayList<PharmaNotification> pharmaNotifications){
        for(PharmaNotification pharmaNotification: pharmaNotifications){
            addPharmaNotification(pharmaNotification);
        }
    }

    public void updatePharmaNotification(PharmaCampainDetails.NotificationStats notificationStats) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PharmaNotification.COLUMN_NAMES.TOT_SENT_NOTIFICATION, notificationStats.getTotalSentNotification());
        values.put(PharmaNotification.COLUMN_NAMES.TOT_PENDING_NOTIFICATION, notificationStats.getTotalPendingNotifcation());
        values.put(PharmaNotification.COLUMN_NAMES.TOT_VIEWED_NOTIFICATION, notificationStats.getTotalViewedNotifcation());
        values.put(PharmaNotification.COLUMN_NAMES.TOT_CONVERTED_TO_APPOINTMENT, notificationStats.getTotalConvertedToAppointment());

        String where = PharmaNotification.COLUMN_NAMES.ID + " = " + notificationStats.getNotificationId();

        // Updating row
        db.update(TABLE_PHARMA_NOTIFICATIONS, values, where, null);
        db.close(); // Closing database connection
    }

    private void addPharmaNotification(PharmaNotification pharmaNotification) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PharmaNotification.COLUMN_NAMES.ID, pharmaNotification.getNotificationId());
        values.put(PharmaNotification.COLUMN_NAMES.NAME, pharmaNotification.getNotificationName());
        values.put(PharmaNotification.COLUMN_NAMES.DESCRIPTION , pharmaNotification.getNotificationDesc());
        values.put(PharmaNotification.COLUMN_NAMES.TYPE_ID, pharmaNotification.getTypeId());
        values.put(PharmaNotification.COLUMN_NAMES.THERAPEUTIC_ID , pharmaNotification.getTherapeuticId());
        values.put(PharmaNotification.COLUMN_NAMES.COMPANY_ID, pharmaNotification.getCompanyId());
        values.put(PharmaNotification.COLUMN_NAMES.UPDATED_ON, pharmaNotification.getUpdatedOn());
        values.put(PharmaNotification.COLUMN_NAMES.UPDATED_BY , pharmaNotification.getUpdatedBy());
        values.put(PharmaNotification.COLUMN_NAMES.CREATED_ON, pharmaNotification.getCreatedOn());
        values.put(PharmaNotification.COLUMN_NAMES.VALID_UPTO, pharmaNotification.getValidUpto());
        values.put(PharmaNotification.COLUMN_NAMES.STATUS, pharmaNotification.getStatus());
        values.put(PharmaNotification.COLUMN_NAMES.EXTERNAL_REF, pharmaNotification.getExternalRef());
        values.put(PharmaNotification.COLUMN_NAMES.COMPANY_NAME, pharmaNotification.getCompanyName());
        values.put(PharmaNotification.COLUMN_NAMES.THERAPEUTIC_NAME, pharmaNotification.getTherapeuticName());
        values.put(PharmaNotification.COLUMN_NAMES.TOT_SENT_NOTIFICATION, pharmaNotification.getTotalSentNotification());
        values.put(PharmaNotification.COLUMN_NAMES.TOT_PENDING_NOTIFICATION, pharmaNotification.getTotalPendingNotifcation());
        values.put(PharmaNotification.COLUMN_NAMES.TOT_VIEWED_NOTIFICATION, pharmaNotification.getTotalViewedNotifcation());
        values.put(PharmaNotification.COLUMN_NAMES.TOT_CONVERTED_TO_APPOINTMENT, pharmaNotification.getTotalConvertedToAppointment());
        values.put(PharmaNotification.COLUMN_NAMES.FILES_LIST, pharmaNotification.getFileList());
        values.put(PharmaNotification.COLUMN_NAMES.THERAPEUTIC_DROPDOWN_VALUES, pharmaNotification.getTherapeuticDropDownValues());

        // Inserting row
        db.insertWithOnConflict(TABLE_PHARMA_NOTIFICATIONS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }

    public void addDoctorContact(String name, String specialist){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DoctorContacts.CONTACTNAME, name);
        values.put(DoctorContacts.CONTACTSPECILIST, specialist);

        //Inserting row
        db.insertWithOnConflict(TABLE_DOCTOR_CONTACTS, null,values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addPharmaDetailedNotifications(ArrayList<PharmaNotification.NotificationDetail> notificationDetails){
        for(PharmaNotification.NotificationDetail notificationDetail: notificationDetails){
            addPharmaDetailedNotification(notificationDetail);
        }
    }

    private void addPharmaDetailedNotification(PharmaNotification.NotificationDetail notificationDetail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.ID, notificationDetail.getDetailId());
        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.NOTIFICATION_ID, notificationDetail.getNotificationId());
        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.CONTENT_TYPE, notificationDetail.getContentType());
        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.CONTENT_SEQ, notificationDetail.getContentSeq());
        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.CONTENT_LOCATION, notificationDetail.getContentLocation());
        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.CONTENT_NAME, notificationDetail.getContentName());
        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.TITLE, notificationDetail.getDetailTitle());
        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.DESCRIPTION, notificationDetail.getDetailDesc());
        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.IMAGE_CONTENT, "");

        // Inserting row
        db.insertWithOnConflict(TABLE_PHARMA_DETAILED_NOTIFICATIONS, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        db.close(); // Closing database connection
    }

    public void updatePharmaDetailedNotificationImage(String bmpBase64, int id){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(PharmaNotification.NotificationDetail.COLUMN_NAMES.IMAGE_CONTENT, bmpBase64);

        String where = PharmaNotification.NotificationDetail.COLUMN_NAMES.ID + " = " + id;
        //Update detailed notification.
        db.update(TABLE_PHARMA_DETAILED_NOTIFICATIONS, values, where, null);
        db.close(); // Closing database connection
    }


    public void addDisplayPicture(Company.DisplayPicture displayPicture){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Company.DisplayPicture.COLUMN_NAMES.ID, displayPicture.getDpId());
        values.put(Company.DisplayPicture.COLUMN_NAMES.LOGIN_ID, displayPicture.getLoginId());
        values.put(Company.DisplayPicture.COLUMN_NAMES.MIME_TYPE, displayPicture.getMimeType());
        values.put(Company.DisplayPicture.COLUMN_NAMES.SECURITY_ID, displayPicture.getSecurityId());
        values.put(Company.DisplayPicture.COLUMN_NAMES.DATA, displayPicture.getData());

        // Inserting row
        db.insertWithOnConflict(TABLE_DISPLAY_PICTURE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }

    public void addLocation(Company.Location location){

        /*
        + Company.Location.COLUMN_NAMES.ID + " INTEGER PRIMARY KEY, "
                + Company.Location.COLUMN_NAMES.ADDRESS1 + " TEXT, "
                + Company.Location.COLUMN_NAMES.ADDRESS2 + " TEXT, "
                + Company.Location.COLUMN_NAMES.CITY + " TEXT, "
                + Company.Location.COLUMN_NAMES.STATE + " TEXT, "
                + Company.Location.COLUMN_NAMES.COUNTRY + " TEXT, "
                + Company.Location.COLUMN_NAMES.ZIP_CODE + " TEXT, "
                + Company.Location.COLUMN_NAMES.TYPE + " TEXT)";
         */

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Company.Location.COLUMN_NAMES.ID, location.getLocationId());
        values.put(Company.Location.COLUMN_NAMES.ADDRESS1, location.getAddress1());
        values.put(Company.Location.COLUMN_NAMES.ADDRESS2, location.getAddress2());
        values.put(Company.Location.COLUMN_NAMES.CITY, location.getCity());
        values.put(Company.Location.COLUMN_NAMES.STATE, location.getState());
        values.put(Company.Location.COLUMN_NAMES.COUNTRY, location.getCountry());
        values.put(Company.Location.COLUMN_NAMES.ZIP_CODE, location.getZipcode());
        values.put(Company.Location.COLUMN_NAMES.TYPE, location.getType());

        // Inserting row
        db.insertWithOnConflict(TABLE_LOCATION, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }


    /**
     * Method adds given company to the database.
     * @param company
     */
    public void addCompany(Company company){

        addDisplayPicture(company.getDisplayPicture());
        addLocation(company.getLocation());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Company.COLUMN_NAMES.ID, company.getCompanyId()); // Company ID
        values.put(Company.COLUMN_NAMES.NAME, company.getCompanyName()); // Company Name
        values.put(Company.COLUMN_NAMES.DESCRIPTION, company.getCompanyDesc());
        values.put(Company.COLUMN_NAMES.CONTACT_NAME, company.getContactName());
        values.put(Company.COLUMN_NAMES.CONTACT_NUMBER, company.getContactNo());
        values.put(Company.COLUMN_NAMES.COMPANY_URL, company.getCompanyUrl());
        values.put(Company.COLUMN_NAMES.STATUS, company.getStatus());
        values.put(Company.COLUMN_NAMES.THERAPEUTIC_AREAS, company.getTherapeuticAreas());
        values.put(Company.COLUMN_NAMES.DP_ID, company.getDisplayPicture().getDpId());
        values.put(Company.COLUMN_NAMES.LOCATION_ID, company.getLocation().getLocationId());

        // Inserting row
        db.insertWithOnConflict(TABLE_COMPANY, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }

    public void addCompanies(ArrayList<Company> companies){
        for(Company company: companies){
            addCompany(company);
        }
    }

    /**
     * Method adds given therapeutic category to the database.
     * @param db
     * @param therapeuticCategory
     */
    public void addTherapeuticAreaDetails(SQLiteDatabase db, TherapeuticCategory therapeuticCategory){
        ContentValues values = new ContentValues();
        values.put(TherapeuticCategory.COLUMN_NAMES.ID, therapeuticCategory.getTherapeuticId()); // Category ID
        values.put(TherapeuticCategory.COLUMN_NAMES.NAME, therapeuticCategory.getTherapeuticName()); // Category Name
        values.put(TherapeuticCategory.COLUMN_NAMES.DESCRIPTION, therapeuticCategory.getTherapeuticDesc()); // Category Name
        values.put(TherapeuticCategory.COLUMN_NAMES.COMPANIES, therapeuticCategory.getCompanies()); // Companies

        // Inserting row
        if(db!=null && db.isOpen())
        db.insertWithOnConflict(TABLE_THERAPEUTIC_CATEGORIES, null, values, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public void  addTherapeuticAreaDetails(ArrayList<TherapeuticCategory> therapeuticCategories){
        SQLiteDatabase db = this.getWritableDatabase();

        for (TherapeuticCategory therapeuticCategory: therapeuticCategories){
            addTherapeuticAreaDetails(db, therapeuticCategory);
        }
        db.close(); // Closing database connection
    }


    /**
     * Method adds given Notification status to the database.
     * @param notificationStatus
     */
    public void addNotificationStatus(NotificationStatus notificationStatus){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NotificationStatus.COLUMN_NAMES.ID, notificationStatus.getId()); // Status ID
        values.put(NotificationStatus.COLUMN_NAMES.STATUS, notificationStatus.getStatus()); // Status

        // Inserting row
        db.insertWithOnConflict(TABLE_NOTIFICATION_STATUS, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }

    /**
     * Method adds given Notification type to the database.
     * @param db
     * @param notificationType
     */
    public void addNotificationType(SQLiteDatabase db, NotificationType notificationType){
        ContentValues values = new ContentValues();
        values.put(NotificationType.COLUMN_NAMES.ID, notificationType.getTypeId()); // Type ID
        values.put(NotificationType.COLUMN_NAMES.NAME, notificationType.getTypeDesc()); // Type ID
        values.put(NotificationType.COLUMN_NAMES.DESCRIPTION, notificationType.getTypeDesc()); // Type ID

        // Inserting row
        db.insertWithOnConflict(TABLE_NOTIFICATION_TYPE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void addNotificationTypes(ArrayList<NotificationType> notificationTypes){

        SQLiteDatabase db = this.getWritableDatabase();

        for(NotificationType notificationType: notificationTypes){
            addNotificationType(db, notificationType);
        }

        db.close();
    }

    public int getDoctorNotificationsLatestUpdatedDate(){

        long lastUpdated = Utils.DEFAULT_START_DATE;

        String projection[] = {Notification.COLUMN_NAMES.UPDATED_ON};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, null, null, null, null, Notification.COLUMN_NAMES.UPDATED_ON);

        Log.i("MedRep", cursor.getCount() + "");

        if(cursor.getCount() > 0 && cursor.moveToFirst()){

            do{
                long temp = Long.parseLong(cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.UPDATED_ON)));

                if(temp > lastUpdated){
                    lastUpdated = temp;
                }
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        if(lastUpdated != -1){
            String str = lastUpdated + "";
            return Integer.parseInt(str.substring(0, 8)) + 1;
        }

        return (int)lastUpdated;
    }


    /**
     * Method adds given notification to the database
     * @param notification
     */
    public void addNotification(Notification notification){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Notification.COLUMN_NAMES.THERAPEUTIC_ID, notification.getTherapeuticId());
        values.put(Notification.COLUMN_NAMES.COMPANY_ID, notification.getCompanyId());
        values.put(Notification.COLUMN_NAMES.NAME, notification.getNotificationName());
        values.put(Notification.COLUMN_NAMES.DESCRIPTION, notification.getNotificationDesc());
        values.put(Notification.COLUMN_NAMES.CREATED_ON, notification.getCreatedOn());
        values.put(Notification.COLUMN_NAMES.CREATED_BY, notification.getCreatedBy());
        values.put(Notification.COLUMN_NAMES.UPDATED_ON, notification.getUpdatedOn());
        values.put(Notification.COLUMN_NAMES.UPDATED_BY, notification.getUpdatedBy());
        values.put(Notification.COLUMN_NAMES.VALID_UPTO, notification.getValidUpto());
        values.put(Notification.COLUMN_NAMES.EXT_REF, notification.getExternalRef());
        values.put(Notification.COLUMN_NAMES.STATUS, notification.getStatus());
        values.put(Notification.COLUMN_NAMES.TYPE_ID, notification.getTypeId());

        if(isNotificationExists(notification.getNotificationId())){
            //Update existing notification

            String where = Notification.COLUMN_NAMES.ID + " = " + notification.getNotificationId();
            db.update(TABLE_NOTIFICATIONS, values, where, null);

        }else{
            //Add notification to database
            values.put(Notification.COLUMN_NAMES.ID, notification.getNotificationId()); // Type ID
            try{
                db.insertWithOnConflict(TABLE_NOTIFICATIONS, null, values, SQLiteDatabase.CONFLICT_ABORT);
            }catch(SQLiteConstraintException e){
                e.printStackTrace();
            }
        }

        // Inserting row

        db.close(); // Closing database connection
    }

    private boolean isNotificationExists(int notificationId) {
        String where = Notification.COLUMN_NAMES.ID + " = " + notificationId;

        String projection[] = {Notification.COLUMN_NAMES.ID};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, Notification.COLUMN_NAMES.ID);
        boolean result = cursor.getCount() > 0;
        cursor.close();

        return result;
    }

    public void addNotifications(ArrayList<Notification> notifications){
        for(Notification notification: notifications){
            addNotification(notification);

            ArrayList<DetailedNotification> detailedNotifications = notification.getNotificationDetails();
            addDetailedNotifications(detailedNotifications);
        }
    }


    /**
     * Method adds given detailed notification to the database
     * @param detailedNotification
     */
    public void addDetailedNotification(DetailedNotification detailedNotification){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DetailedNotification.COLUMN_NAMES.ID, detailedNotification.getDetailId()); // Type ID
        values.put(DetailedNotification.COLUMN_NAMES.NOTIFICATION_ID, detailedNotification.getNotificationId());
        values.put(DetailedNotification.COLUMN_NAMES.CONTENT_TYPE, detailedNotification.getContentType());
        values.put(DetailedNotification.COLUMN_NAMES.CONTENT_SEQ, detailedNotification.getContentSeq());
        values.put(DetailedNotification.COLUMN_NAMES.CONTENT_LOC, detailedNotification.getContentLocation());
        values.put(DetailedNotification.COLUMN_NAMES.CONTENT_NAME, detailedNotification.getContentName());
        values.put(DetailedNotification.COLUMN_NAMES.TITLE, detailedNotification.getDetailTitle());
        values.put(DetailedNotification.COLUMN_NAMES.DESCRIPTION, detailedNotification.getDetailDesc());

        // Inserting row
        db.insertWithOnConflict(TABLE_DETAILED_NOTIFICATION, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }

    public void addDetailedNotifications(ArrayList<DetailedNotification> detailedNotifications){
        for(DetailedNotification detailedNotification: detailedNotifications){
            addDetailedNotification(detailedNotification);
        }
    }


    public ArrayList<Notification> getNotifications(int companyId) {
        ArrayList<Notification> notifications = null;

        String where = Notification.COLUMN_NAMES.COMPANY_ID + " = " + companyId;

        String projection[] = {Notification.COLUMN_NAMES.ID, Notification.COLUMN_NAMES.NAME,
                Notification.COLUMN_NAMES.DESCRIPTION, Notification.COLUMN_NAMES.THERAPEUTIC_ID,
                Notification.COLUMN_NAMES.IS_READ, Notification.COLUMN_NAMES.IS_FAVOURITE, Notification.COLUMN_NAMES.IS_REMINDER_ADDED};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, Notification.COLUMN_NAMES.UPDATED_ON);

        Log.i("MedRep", cursor.getCount() + "");

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            notifications = new ArrayList<>();
            do {
                Notification notification = new Notification();
                notification.setCompanyId(companyId);;
                notification.setNotificationId(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.ID)));
                notification.setNotificationName(cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.NAME)));
                notification.setNotificationDesc(cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.DESCRIPTION)));
                notification.setTherapeuticId(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.THERAPEUTIC_ID)));
                notification.setIsRead(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_READ)) > 0);
                notification.setIsFavourite(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_FAVOURITE)) > 0);
                notification.setIsReminderAdded(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_REMINDER_ADDED)) > 0);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        return notifications;
    }

    public ArrayList<Notification> getSelectedNotifications(int[] notificationIDS, int therapeuticID, int companyID) {
        ArrayList<Notification> notifications = null;

        String where;

        if(therapeuticID == -1){
            where = Notification.COLUMN_NAMES.COMPANY_ID + " = " + companyID;
        }else{
            where = Notification.COLUMN_NAMES.THERAPEUTIC_ID + " = " + therapeuticID +
                    " AND " + Notification.COLUMN_NAMES.COMPANY_ID + " = " + companyID;
        }

        System.out.println("where: " + where);

        String projection[] = {Notification.COLUMN_NAMES.ID, Notification.COLUMN_NAMES.NAME,
                Notification.COLUMN_NAMES.DESCRIPTION,
                Notification.COLUMN_NAMES.IS_READ, Notification.COLUMN_NAMES.IS_FAVOURITE};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, Notification.COLUMN_NAMES.UPDATED_ON);

        Log.i("MedRep", cursor.getCount() + "");

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            notifications = new ArrayList<>();
            do {
                Notification notification = new Notification();
//                notification.setCompanyId(companyId + "");
                notification.setNotificationId(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.ID)));
                notification.setNotificationName(cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.NAME)));
                notification.setNotificationDesc(cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.DESCRIPTION)));
                notification.setIsRead(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_READ)) > 0);
                notification.setIsFavourite(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_FAVOURITE)) > 0);
                notification.setTherapeuticId(therapeuticID);
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        return notifications;
    }

    public ArrayList<TherapeuticCategory> getTherapeuticAreaDetails(int[] therapeuticIDS){
        ArrayList<TherapeuticCategory> therapeuticCategories = null;

        String inClause = Utils.GET_IN_CLAUSE(therapeuticIDS);

        String where = TherapeuticCategory.COLUMN_NAMES.ID + " in " + inClause;

//        String projection[] = {Notification.COLUMN_NAMES.ID, Notification.COLUMN_NAMES.THERAPEUTIC_ID};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_THERAPEUTIC_CATEGORIES, null, where, null, null, null, TherapeuticCategory.COLUMN_NAMES.ID);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            therapeuticCategories = new ArrayList<>();
            do {

                int id = cursor.getInt(cursor.getColumnIndex(TherapeuticCategory.COLUMN_NAMES.ID));
                String name = cursor.getString(cursor.getColumnIndex(TherapeuticCategory.COLUMN_NAMES.NAME));
                String description = cursor.getString(cursor.getColumnIndex(TherapeuticCategory.COLUMN_NAMES.DESCRIPTION));
                String companies = cursor.getString(cursor.getColumnIndex(TherapeuticCategory.COLUMN_NAMES.COMPANIES));

               TherapeuticCategory therapeuticCategory = new TherapeuticCategory();
                therapeuticCategory.setTherapeuticId(id);
                therapeuticCategory.setTherapeuticName(name);
                therapeuticCategory.setTherapeuticDesc(description);
                therapeuticCategory.setCompanies(companies);

                therapeuticCategories.add(therapeuticCategory);
            } while (cursor.moveToNext());
        }

        return therapeuticCategories;
    }

    public int[] getNotificationIDs(int[] notificationIDS, int therapeuticID){

        int[] resultIDS = null;

        String inClause = Utils.GET_IN_CLAUSE(notificationIDS);

        String where = Notification.COLUMN_NAMES.THERAPEUTIC_ID + " = " + therapeuticID + " AND " + Notification.COLUMN_NAMES.ID + " in " + inClause;

        String projection[] = {Notification.COLUMN_NAMES.ID};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, Notification.COLUMN_NAMES.ID);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            resultIDS = new int[cursor.getCount()];
            do {
                Notification notification = new Notification();

                resultIDS[cursor.getPosition()] = cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.ID));
            } while (cursor.moveToNext());
        }

        return resultIDS;
    }

    public ArrayList<DetailedNotification> getDetailedNotification(int[] notificationIDs){
        ArrayList<DetailedNotification> detailedNotifications = null;

        String inClause = Utils.GET_IN_CLAUSE(notificationIDs);

        String where = DetailedNotification.COLUMN_NAMES.NOTIFICATION_ID + " in " + inClause;

//        String projection[] = {Notification.COLUMN_NAMES.ID, Notification.COLUMN_NAMES.THERAPEUTIC_ID};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DETAILED_NOTIFICATION, null, where, null, null, null, DetailedNotification.COLUMN_NAMES.ID);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            detailedNotifications = new ArrayList<>();
            do {

                int id = cursor.getInt(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.ID));
                int notificationID = cursor.getInt(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.NOTIFICATION_ID));
                String title = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.DESCRIPTION));
                String contentType = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.CONTENT_TYPE));
                int contentSeq = cursor.getInt(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.CONTENT_SEQ));
                String contentLocation = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.CONTENT_LOC));
                String contentName = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.CONTENT_NAME));
                String imageContent = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.IMAGE_CONTENT));

                DetailedNotification detailedNotification = new DetailedNotification(
                        id, notificationID, title, description, contentType, contentSeq,
                        contentLocation, contentName, imageContent);

                detailedNotifications.add(detailedNotification);
            } while (cursor.moveToNext());
        }

        return detailedNotifications;
    }

    public void updateDetailedNotification(ContentValues contentValues, int id){
        String where = DetailedNotification.COLUMN_NAMES.ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_DETAILED_NOTIFICATION, contentValues, where, null);
        db.close();
    }

    public void markNotificationAsFavourite(int id){
        String where = Notification.COLUMN_NAMES.ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Notification.COLUMN_NAMES.IS_FAVOURITE, true);
        db.update(TABLE_NOTIFICATIONS, values, where, null);
        db.close();
    }

    public void markNotificationAsNotFavourite(int id){
        String where = Notification.COLUMN_NAMES.ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Notification.COLUMN_NAMES.IS_FAVOURITE, false);
        db.update(TABLE_NOTIFICATIONS, values, where, null);
        db.close();
    }

    public boolean isThisNotificationFavourite(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {Notification.COLUMN_NAMES.IS_FAVOURITE};

        String where = Notification.COLUMN_NAMES.ID + " = " + id;

        boolean isFavourite = false;
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, null);
        if(cursor!=null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                isFavourite = cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_FAVOURITE)) > 0;
            }
            cursor.close();
        }
        return isFavourite;
    }

    public ArrayList<Company> getAllAvailableCompanies(){
        ArrayList<Company> companies = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_COMPANY, null, null, null, null, null, null);

        if(cursor.getCount() > 0 && cursor.moveToFirst()){
            do{
                Company company = new Company();

                company.setCompanyId(cursor.getInt(cursor.getColumnIndex(Company.COLUMN_NAMES.ID)));
                company.setCompanyName(cursor.getString(cursor.getColumnIndex(Company.COLUMN_NAMES.NAME)));
                company.setCompanyDesc(cursor.getString(cursor.getColumnIndex(Company.COLUMN_NAMES.DESCRIPTION)));
                company.setContactName(cursor.getString(cursor.getColumnIndex(Company.COLUMN_NAMES.CONTACT_NAME)));
                company.setContactNo(cursor.getString(cursor.getColumnIndex(Company.COLUMN_NAMES.CONTACT_NUMBER)));
                company.setCompanyUrl(cursor.getString(cursor.getColumnIndex(Company.COLUMN_NAMES.COMPANY_URL)));
                company.setStatus(cursor.getString(cursor.getColumnIndex(Company.COLUMN_NAMES.STATUS)));
                company.setTherapeuticAreas(cursor.getString(cursor.getColumnIndex(Company.COLUMN_NAMES.THERAPEUTIC_AREAS)));

                int dpID = cursor.getInt(cursor.getColumnIndex(Company.COLUMN_NAMES.DP_ID));
                company.setDisplayPicture(getDisplayPicture(dpID));

                int locationID = cursor.getInt(cursor.getColumnIndex(Company.COLUMN_NAMES.LOCATION_ID));
                company.setLocation(getLocation(locationID));

                companies.add(company);

            }while(cursor.moveToNext());
        }

        cursor.close();

        return companies;
    }

    private Company.Location getLocation(int locationID) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {Company.Location.COLUMN_NAMES.ADDRESS1,
                Company.Location.COLUMN_NAMES.ADDRESS2,
                Company.Location.COLUMN_NAMES.CITY,
                Company.Location.COLUMN_NAMES.STATE,
                Company.Location.COLUMN_NAMES.COUNTRY,
                Company.Location.COLUMN_NAMES.ZIP_CODE,
                Company.Location.COLUMN_NAMES.TYPE };

        String where = Company.Location.COLUMN_NAMES.ID + " = " + locationID;

        Cursor cursor = db.query(TABLE_LOCATION, projection, where, null, null, null, null);

        Company.Location location = null;

        if(cursor.getCount() > 0 && cursor.moveToFirst()){
            String address1 = cursor.getString(cursor.getColumnIndex(Company.Location.COLUMN_NAMES.ADDRESS1));
            String address2 = cursor.getString(cursor.getColumnIndex(Company.Location.COLUMN_NAMES.ADDRESS2));
            String city = cursor.getString(cursor.getColumnIndex(Company.Location.COLUMN_NAMES.CITY));
            String state = cursor.getString(cursor.getColumnIndex(Company.Location.COLUMN_NAMES.STATE));
            String country = cursor.getString(cursor.getColumnIndex(Company.Location.COLUMN_NAMES.COUNTRY));
            String type = cursor.getString(cursor.getColumnIndex(Company.Location.COLUMN_NAMES.TYPE));

            location = new Company.Location();
            location.setAddress1(address1);
            location.setAddress2(address2);
            location.setCity(city);
            location.setState(state);
            location.setCountry(country);
            location.setType(type);
        }

        return location;
    }

    private Company.DisplayPicture getDisplayPicture(int dpID){
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {Company.DisplayPicture.COLUMN_NAMES.DATA ,
                Company.DisplayPicture.COLUMN_NAMES.MIME_TYPE ,
                Company.DisplayPicture.COLUMN_NAMES.LOGIN_ID,
                Company.DisplayPicture.COLUMN_NAMES.SECURITY_ID};

        String where = Company.DisplayPicture.COLUMN_NAMES.ID + " = " + dpID;

        Cursor cursor = db.query(TABLE_DISPLAY_PICTURE, projection, where, null, null, null, null);

        Company.DisplayPicture displayPicture = null;

        if(cursor.getCount() > 0 && cursor.moveToFirst()){
            String data = cursor.getString(cursor.getColumnIndex(Company.DisplayPicture.COLUMN_NAMES.DATA));
            String mimeTye = cursor.getString(cursor.getColumnIndex(Company.DisplayPicture.COLUMN_NAMES.MIME_TYPE));
            int loginID = cursor.getInt(cursor.getColumnIndex(Company.DisplayPicture.COLUMN_NAMES.LOGIN_ID));
            int securityID = cursor.getInt(cursor.getColumnIndex(Company.DisplayPicture.COLUMN_NAMES.SECURITY_ID));

            displayPicture = new Company.DisplayPicture();
            displayPicture.setDpId(dpID);
            displayPicture.setData(data);
            displayPicture.setMimeType(mimeTye);
            displayPicture.setLoginId(loginID + "");
            displayPicture.setSecurityId(securityID + "");
        }

        cursor.close();;
        return displayPicture;
    }

    public ArrayList<Company> getCompaniesWithNotifications(ArrayList<Company> companies){
        ArrayList<Company> companiesWithNotifications = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {Notification.COLUMN_NAMES.COMPANY_ID};

        for(Company company: companies){
            String where = Notification.COLUMN_NAMES.COMPANY_ID + " = " + company.getCompanyId();

            Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, null);
            if(cursor.getCount() > 0){
                companiesWithNotifications.add(company);
            }
            cursor.close();
        }


        return companiesWithNotifications;
    }

    public ArrayList<DetailedNotification> getDetailedNotifications(int notificationID){
        ArrayList<DetailedNotification> detailedNotifications = null;

        String where = DetailedNotification.COLUMN_NAMES.NOTIFICATION_ID + " = " + notificationID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DETAILED_NOTIFICATION, null, where, null, null, null, DetailedNotification.COLUMN_NAMES.ID);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            detailedNotifications = new ArrayList<>();
            do {

                int id = cursor.getInt(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.ID));
                String title = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.TITLE));
                String description = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.DESCRIPTION));
                String contentType = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.CONTENT_TYPE));
                int contentSeq = cursor.getInt(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.CONTENT_SEQ));
                String contentLocation = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.CONTENT_LOC));
                String contentName = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.CONTENT_NAME));
                String imageContent = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.IMAGE_CONTENT));

                DetailedNotification detailedNotification = new DetailedNotification(
                        id, notificationID, title, description, contentType, contentSeq,
                        contentLocation, contentName, imageContent);

                detailedNotifications.add(detailedNotification);
            } while (cursor.moveToNext());
        }


        return detailedNotifications;
    }

    public Bitmap getPharmaDetailedNotificationImage(int id){

        Bitmap bmp = null;

        String where = PharmaNotification.NotificationDetail.COLUMN_NAMES.ID + " = " + id;

        String projection[] = {PharmaNotification.NotificationDetail.COLUMN_NAMES.IMAGE_CONTENT};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHARMA_DETAILED_NOTIFICATIONS, projection, where, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            String str = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.IMAGE_CONTENT));
            if(str != null && !str.trim().equals("") && !str.trim().equals("null")){
                bmp = Utils.decodeBase64(str, false);
            }
        }

        cursor.close();
        return bmp;
    }

    public Bitmap getDetailedNotificationImage(int id){

        Bitmap bmp = null;

        String where = DetailedNotification.COLUMN_NAMES.ID + " = " + id;

        String projection[] = {DetailedNotification.COLUMN_NAMES.IMAGE_CONTENT};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DETAILED_NOTIFICATION, projection, where, null, null, null, null);

        // looping through all rows and adding to list
        if(!cursor.isClosed())
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            String str = cursor.getString(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.IMAGE_CONTENT));

            System.out.println("Bitmap str:: " + str);
            if(str != null && !str.trim().equals("") && !str.trim().equals("null")){
                bmp = Utils.decodeBase64(str, false);
            }
        }

        cursor.close();
        return bmp;
    }

    public int getNotificationID(int id){
        String where = DetailedNotification.COLUMN_NAMES.ID + " = " + id;

        String projection[] = {DetailedNotification.COLUMN_NAMES.NOTIFICATION_ID};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DETAILED_NOTIFICATION, projection, where, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            int notificationID = cursor.getInt(cursor.getColumnIndex(DetailedNotification.COLUMN_NAMES.NOTIFICATION_ID));
            cursor.close();

            return notificationID;
        }


        return -1;
    }

    public String getNotificationName(int id){
        String where = Notification.COLUMN_NAMES.ID + " = " + id;

        String projection[] = {Notification.COLUMN_NAMES.NAME};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.NAME));
            cursor.close();

            return name;
        }
        return null;
    }

    private int getCompanyID(int id){
        String where = Notification.COLUMN_NAMES.ID + " = " + id;

        String projection[] = {Notification.COLUMN_NAMES.COMPANY_ID};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            int companyID = cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.COMPANY_ID));
            cursor.close();

           return companyID;
        }
        return -1;
    }


    public String getCompanyName(int notificationID){

        int companyID = getCompanyID(notificationID);

        if(companyID != -1){
            String where = Company.COLUMN_NAMES.ID + " = " + companyID;

            String projection[] = {Company.COLUMN_NAMES.NAME};

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_COMPANY, projection, where, null, null, null, null);

            // looping through all rows and adding to list
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                String companyName = cursor.getString(cursor.getColumnIndex(Company.COLUMN_NAMES.NAME));
                cursor.close();

                return companyName;
            }
        }
        return null;
    }

    private int getTherapeuticID(int id){
        String where = Notification.COLUMN_NAMES.ID + " = " + id;

        String projection[] = {Notification.COLUMN_NAMES.THERAPEUTIC_ID};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, null);

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            int therapeuticID = cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.THERAPEUTIC_ID));
            cursor.close();

            return therapeuticID;
        }
        return -1;
    }


    public String getTherapeuticName(int notificationID){

        int therapeuticID = getTherapeuticID(notificationID);

        if(therapeuticID != -1){
            String where = TherapeuticCategory.COLUMN_NAMES.ID + " = " + therapeuticID;

            String projection[] = {TherapeuticCategory.COLUMN_NAMES.NAME};

            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.query(TABLE_THERAPEUTIC_CATEGORIES, projection, where, null, null, null, null);

            // looping through all rows and adding to list
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                String therapeuticName = cursor.getString(cursor.getColumnIndex(TherapeuticCategory.COLUMN_NAMES.NAME));
                cursor.close();

                return therapeuticName;
            }
        }
        return null;
    }

    public ArrayList<Notification> getFavouriteNotifications() {
        ArrayList<Notification> notifications = null;

        String where = Notification.COLUMN_NAMES.IS_FAVOURITE + " > " + 0;

        String projection[] = {Notification.COLUMN_NAMES.ID,
                Notification.COLUMN_NAMES.NAME,
                Notification.COLUMN_NAMES.DESCRIPTION,
                Notification.COLUMN_NAMES.IS_READ,
                Notification.COLUMN_NAMES.THERAPEUTIC_ID};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, Notification.COLUMN_NAMES.UPDATED_ON);

        Log.i("MedRep", cursor.getCount() + "");

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            notifications = new ArrayList<>();
            do {
                Notification notification = new Notification();
//                notification.setCompanyId(companyId + "");
                notification.setNotificationId(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.ID)));
                notification.setNotificationName(cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.NAME)));
                notification.setNotificationDesc(cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.DESCRIPTION)));
                notification.setIsRead(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_READ)) > 0);
                notification.setIsFavourite(true);
                notification.setTherapeuticId(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.THERAPEUTIC_ID)));
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        return notifications;
    }

    public ArrayList<Notification> getReadNotifications() {
        ArrayList<Notification> notifications = null;

        String where = Notification.COLUMN_NAMES.IS_READ + " > " + 0;

        String projection[] = {Notification.COLUMN_NAMES.ID,
                Notification.COLUMN_NAMES.NAME,
                Notification.COLUMN_NAMES.DESCRIPTION,
                Notification.COLUMN_NAMES.IS_FAVOURITE,
                Notification.COLUMN_NAMES.THERAPEUTIC_ID};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, Notification.COLUMN_NAMES.UPDATED_ON);

        Log.i("MedRep", cursor.getCount() + "");

        // looping through all rows and adding to list
        if (cursor.getCount() > 0 && cursor.moveToFirst()) {
            notifications = new ArrayList<>();
            do {
                Notification notification = new Notification();
//                notification.setCompanyId(companyId + "");
                notification.setNotificationId(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.ID)));
                notification.setNotificationName(cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.NAME)));
                notification.setNotificationDesc(cursor.getString(cursor.getColumnIndex(Notification.COLUMN_NAMES.DESCRIPTION)));
                notification.setIsRead(true);
                notification.setIsFavourite(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_FAVOURITE)) > 0);
                notification.setTherapeuticId(cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.THERAPEUTIC_ID)));
                notifications.add(notification);
            } while (cursor.moveToNext());
        }

        return notifications;
    }

    public void updateNotification(ContentValues values, int id) {
        String where = Notification.COLUMN_NAMES.ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_NOTIFICATIONS, values, where, null);
        db.close();
    }

    public boolean isReminderAdded(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {Notification.COLUMN_NAMES.IS_REMINDER_ADDED};

        String where = Notification.COLUMN_NAMES.ID + " = " + id;

        boolean result = false;
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            result = cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_REMINDER_ADDED)) > 0;
        }
        cursor.close();

        return result;
    }

    public boolean isFeedbackGiven(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {Notification.COLUMN_NAMES.IS_FEEDBACK_GIVEN};

        String where = Notification.COLUMN_NAMES.ID + " = " + id;

        boolean result = false;
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            result = cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_FEEDBACK_GIVEN)) > 0;
        }
        cursor.close();

        return result;
    }

    public boolean isNotificationRead(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {Notification.COLUMN_NAMES.IS_READ};

        String where = Notification.COLUMN_NAMES.ID + " = " + id;

        boolean result = false;
        Cursor cursor = db.query(TABLE_NOTIFICATIONS, projection, where, null, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();

            result = cursor.getInt(cursor.getColumnIndex(Notification.COLUMN_NAMES.IS_READ)) > 0;
        }
        cursor.close();

        return result;
    }

    public void markFeedbackGiven(int id){
        String where = Notification.COLUMN_NAMES.ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Notification.COLUMN_NAMES.IS_FEEDBACK_GIVEN, true);
        db.update(TABLE_NOTIFICATIONS, values, where, null);
        db.close();
    }

    public void markReminderAdded(int id){
        String where = Notification.COLUMN_NAMES.ID + " = " + id;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Notification.COLUMN_NAMES.IS_REMINDER_ADDED, true);
        db.update(TABLE_NOTIFICATIONS, values, where, null);
        db.close();
    }
}
