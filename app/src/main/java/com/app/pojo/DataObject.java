package com.app.pojo;

/**
 * Created by masood on 9/5/15.
 */
public class DataObject {
    private String mText1;
    private int drawableId;

    public DataObject(String text1, int drawableId){
        mText1 = text1;
        this.drawableId = drawableId;
    }

    public String getmText1() {
        return mText1;
    }

    public void setmText1(String mText1) {
        this.mText1 = mText1;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }
}
