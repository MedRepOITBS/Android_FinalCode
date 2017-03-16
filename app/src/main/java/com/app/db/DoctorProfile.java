package com.app.db;

import java.util.ArrayList;

/**
 * Created by kishore on 10/10/15.
 */
public class DoctorProfile {
    private String username;
    private String password;
    private String loginTime;
    private String userSecurityId;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;
    private int roleId;
    private String roleName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String alias;
    private String title;
    private String phoneNo;
    private String mobileNo;
    private String emailId;
    private String alternateEmailId;
    private ProfilePicture profilePicture;
    private String status;
    private String therapeuticName;
    private ArrayList<Company.Location> locations;
    private int doctorId;

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public ArrayList<Company.Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Company.Location> locations) {
        this.locations = locations;
    }

    public String getTherapeuticName() {
        return therapeuticName;
    }

    public void setTherapeuticName(String therapeuticName) {
        this.therapeuticName = therapeuticName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getUserSecurityId() {
        return userSecurityId;
    }

    public void setUserSecurityId(String userSecurityId) {
        this.userSecurityId = userSecurityId;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getAlternateEmailId() {
        return alternateEmailId;
    }

    public void setAlternateEmailId(String alternateEmailId) {
        this.alternateEmailId = alternateEmailId;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class ProfilePicture{
        private int dpId;
        private String data;
        private String mimeType;
        private int loginId;
        private int securityId;

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

        public int getLoginId() {
            return loginId;
        }

        public void setLoginId(int loginId) {
            this.loginId = loginId;
        }

        public int getSecurityId() {
            return securityId;
        }

        public void setSecurityId(int securityId) {
            this.securityId = securityId;
        }
    }
}


