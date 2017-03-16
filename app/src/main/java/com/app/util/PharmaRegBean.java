package com.app.util;

import java.io.Serializable;

/**
 * Created by Vishnu on 28-09-2015.
 */


public class PharmaRegBean implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String strProfileName;
    private String strPassString;
    private  String mdEmailId;
    private String mdFirstName;
    private String mdLastName;
    private String mdMobileNumber;
    private String mdAreaCovered;
    private String mdRmName;
    private String rmMail;
    private String rmMobile;

    private String mdaddress1;
    private String mmdAddress2;
    private String mdState;
    private String mdCity;
    private String mdZipcode;

    private String mdCompanyName;
    //New Survey Application Form Bean

    static PharmaRegBean mProperyBean=null;



    //Instance
    static public PharmaRegBean getInstance() {

        if(mProperyBean==null)
            mProperyBean=new PharmaRegBean();
        return mProperyBean;
    }

    public String getMdCompanyName() {
        return mdCompanyName;
    }

    public void setMdCompanyName(String mdCompanyName) {
        this.mdCompanyName = mdCompanyName;
    }

    public String getStrProfileName() {
        return strProfileName;
    }

    public void setStrProfileName(String strProfileName) {
        this.strProfileName = strProfileName;
    }

    public static PharmaRegBean getmProperyBean() {
        return mProperyBean;
    }

    public static void setmProperyBean(PharmaRegBean mProperyBean) {
        PharmaRegBean.mProperyBean = mProperyBean;
    }

    public String getStrPassString() {
        return strPassString;
    }

    public void setStrPassString(String strPassString) {
        this.strPassString = strPassString;
    }

    public String getMdaddress1() {
        return mdaddress1;
    }

    public void setMdaddress1(String mdaddress1) {
        this.mdaddress1 = mdaddress1;
    }

    public String getMmdAddress2() {
        return mmdAddress2;
    }

    public void setMmdAddress2(String mmdAddress2) {
        this.mmdAddress2 = mmdAddress2;
    }

    public String getMdState() {
        return mdState;
    }

    public void setMdState(String mdState) {
        this.mdState = mdState;
    }

    public String getMdCity() {
        return mdCity;
    }

    public void setMdCity(String mdCity) {
        this.mdCity = mdCity;
    }

    public String getMdZipcode() {
        return mdZipcode;
    }

    public void setMdZipcode(String mdZipcode) {
        this.mdZipcode = mdZipcode;
    }

    public String getMdRmName() {
        return mdRmName;
    }

    public void setMdRmName(String mdRmName) {
        this.mdRmName = mdRmName;
    }

    public String getRmMail() {
        return rmMail;
    }

    public void setRmMail(String rmMail) {
        this.rmMail = rmMail;
    }

    public String getRmMobile() {
        return rmMobile;
    }

    public void setRmMobile(String rmMobile) {
        this.rmMobile = rmMobile;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMdEmailId() {
        return mdEmailId;
    }

    public void setMdEmailId(String mdEmailId) {
        this.mdEmailId = mdEmailId;
    }

    public String getMdFirstName() {
        return mdFirstName;
    }

    public void setMdFirstName(String mdFirstName) {
        this.mdFirstName = mdFirstName;
    }

    public String getMdLastName() {
        return mdLastName;
    }

    public void setMdLastName(String mdLastName) {
        this.mdLastName = mdLastName;
    }

    public String getMdMobileNumber() {
        return mdMobileNumber;
    }

    public void setMdMobileNumber(String mdMobileNumber) {
        this.mdMobileNumber = mdMobileNumber;
    }

    public String getMdAreaCovered() {
        return mdAreaCovered;
    }

    public void setMdAreaCovered(String mdAreaCovered) {
        this.mdAreaCovered = mdAreaCovered;
    }
}

