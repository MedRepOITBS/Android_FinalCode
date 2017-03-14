package com.app.db;

/**
 * Created by Kishore on 9/23/2015.
 */
public class TherapeuticCategory {
    private int therapeuticId;
    private String therapeuticName;
    private String therapeuticDesc;
    private String companies;

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

    public String getTherapeuticDesc() {
        return therapeuticDesc;
    }

    public void setTherapeuticDesc(String therapeuticDesc) {
        this.therapeuticDesc = therapeuticDesc;
    }

    public String getCompanies() {
        return companies;
    }

    public void setCompanies(String companies) {
        this.companies = companies;
    }

    public static final class COLUMN_NAMES{
        public static final String ID = "ID";
        public static final String NAME = "Name";
        public static final String DESCRIPTION = "Description";
        public static final String COMPANIES = "Companies";
        public static final String COMPANY_ID = "CompanyID";
    }

}
