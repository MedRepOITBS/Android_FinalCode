package pharma.model;

import com.app.db.Company;
import com.app.db.DoctorProfile;

import java.util.ArrayList;

/**
 * Created by kishore on 31/10/15.
 */
public class PharmaRepProfile{
    private String username;
    private String password;
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
    private DoctorProfile.ProfilePicture profilePicture;
    private String status;
    private ArrayList<Company.Location> locations;
    private int repId;
    private int companyId;
    private String companyName;
    private String coveredArea;
    private int managerId;
    private String coveredZone;
    private int therapeuticId;
    private String therapeuticName;
    private String managerEmail;

    public DoctorProfile.ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(DoctorProfile.ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Company.Location> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Company.Location> locations) {
        this.locations = locations;
    }

    public int getRepId() {
        return repId;
    }

    public void setRepId(int repId) {
        this.repId = repId;
    }

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

    public String getCoveredArea() {
        return coveredArea;
    }

    public void setCoveredArea(String coveredArea) {
        this.coveredArea = coveredArea;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    public String getCoveredZone() {
        return coveredZone;
    }

    public void setCoveredZone(String coveredZone) {
        this.coveredZone = coveredZone;
    }

    public int getTherapeuticId() {
        return therapeuticId;
    }

    public void setTherapeuticId(int therapeuticId) {
        this.therapeuticId = therapeuticId;
    }

    public String getTherapeuticName() {
        return therapeuticName;
    }

    public void setTherapeuticName(String therapeuticName) {
        this.therapeuticName = therapeuticName;
    }

    public String getManagerEmail() {
        return managerEmail;
    }

    public void setManagerEmail(String managerEmail) {
        this.managerEmail = managerEmail;
    }
}