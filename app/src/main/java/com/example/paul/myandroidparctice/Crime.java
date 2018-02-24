package com.example.paul.myandroidparctice;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private int mRequiresPolice;
    private String mSuspect;

    public Crime(){
//        mId = UUID.randomUUID();
//        mDate = new Date();
        this(UUID.randomUUID());
    }

    public Crime(UUID id){
        mId = id;
        mDate = new Date();
    }



    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    public int getRequiresPolice() {
        return mRequiresPolice;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public void setRequiresPolice(int requiresPolice) {
        mRequiresPolice = requiresPolice;
    }

    public String getPhotoFileName(){
        return "IMG_"+getId().toString()+".jpg";
    }

}
