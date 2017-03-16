package com.app.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by masood on 9/6/15.
 */
public class SurveryList implements Parcelable {

    private ArrayList<Survery> surveryArrayList;

    public SurveryList(){

    }

    public SurveryList(Parcel in){
        surveryArrayList = new ArrayList<>();
        in.readList(surveryArrayList, Survery.class.getClassLoader());
    }

    public void setSurveryArrayList(ArrayList<Survery> surveryArrayList){
        this.surveryArrayList = surveryArrayList;
    }

    public ArrayList<Survery> getSurveryArrayList(){
        return surveryArrayList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(surveryArrayList);
    }

    public static final Parcelable.Creator<SurveryList> CREATOR = new Parcelable.Creator<SurveryList>()
    {
        @Override
        public SurveryList createFromParcel(Parcel in)
        {
            return new SurveryList(in);
        }

        @Override
        public SurveryList[] newArray(int size)
        {
            return new SurveryList[size];
        }
    };
}
