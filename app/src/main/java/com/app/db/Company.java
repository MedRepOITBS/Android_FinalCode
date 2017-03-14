package com.app.db;

/**
 * Created by Kishore on 9/23/2015.
 */
public class Company {

    private int companyId;
    private String companyName;
    private String companyDesc;
    private String contactName;
    private String contactNo;
    private DisplayPicture displayPicture;
    private Location location;
    private String companyUrl;
    private String status;
    private String therapeuticAreas;

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyDesc() {
        return companyDesc;
    }

    public void setCompanyDesc(String companyDesc) {
        this.companyDesc = companyDesc;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public DisplayPicture getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(DisplayPicture displayPicture) {
        this.displayPicture = displayPicture;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTherapeuticAreas() {
        return therapeuticAreas;
    }

    public void setTherapeuticAreas(String therapeuticAreas) {
        this.therapeuticAreas = therapeuticAreas;
    }


    public static class COLUMN_NAMES{
        public static final String ID = "ID";
        public static final String NAME = "NAme";
        public static final String DESCRIPTION = "Description";
        public static final String CONTACT_NAME = "ContactName";
        public static final String CONTACT_NUMBER = "ContactNumber";
        public static final String COMPANY_URL = "CompanyURL";
        public static final String STATUS = "Status";
        public static final String THERAPEUTIC_AREAS = "TherapeuticAreas";
        public static final String DP_ID = "dpID";
        public static final String LOCATION_ID = "LocationID";


    }

    public static final class DisplayPicture{
        private int dpId;
        private String data;
        private String mimeType;
        private String loginId;
        private String securityId;

        public int getDpId() {
            return dpId;
        }

        public void setDpId(int dpId) {
            this.dpId = dpId;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public String getLoginId() {
            return loginId;
        }

        public void setLoginId(String loginId) {
            this.loginId = loginId;
        }

        public String getSecurityId() {
            return securityId;
        }

        public void setSecurityId(String securityId) {
            this.securityId = securityId;
        }

        public static final class COLUMN_NAMES{
            public static final String ID = "ID";
            public static final String DATA = "Data";
            public static final String MIME_TYPE = "MimeType";
            public static final String LOGIN_ID = "LoginID";
            public static final String SECURITY_ID = "SecurityID";
        }

    }

    public static final class Location{
        private int locationId;
        private String address1;
        private String address2;
        private String city;
        private String state;
        private String country;
        private String zipcode;
        private String type;

        public int getLocationId() {
            return locationId;
        }

        public void setLocationId(int locationId) {
            this.locationId = locationId;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public static final class COLUMN_NAMES{
            public static final String ID = "ID";
            public static final String ADDRESS1 = "Address1";
            public static final String ADDRESS2 = "Address2";
            public static final String CITY = "City";
            public static final String STATE = "State";
            public static final String COUNTRY = "Country";
            public static final String ZIP_CODE = "ZipCode";
            public static final String TYPE = "Type";
        }
    }


}
