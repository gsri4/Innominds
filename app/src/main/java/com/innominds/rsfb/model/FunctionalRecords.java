package com.innominds.rsfb.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sgajula on 24/02/16.
 */

@Table(name = "FunctionalRecords")
public class FunctionalRecords extends Model {

    @Column(name = "functionalarea")
    public String mFunctionalArea;

    @Column(name = "categorycode")
    public String mCategoryCode;


    @Column(name = "recvalue")
    public String mRecValue;

    @Column(name = "recVital")
    public String mRecVital;


    public FunctionalRecords() {

        super();
    }

    public String getmFunctionalArea() {
        return mFunctionalArea;
    }

    public void setmFunctionalArea(String mFunctionalArea) {
        this.mFunctionalArea = mFunctionalArea;
    }

    public String getmRecVital() {
        return mRecVital;
    }

    public void setmRecVital(String mRecVital) {
        this.mRecVital = mRecVital;
    }

    public String getmRecValue() {
        return mRecValue;
    }

    public void setmRecValue(String mRecValue) {
        this.mRecValue = mRecValue;
    }

    public String getmCategoryCode() {
        return mCategoryCode;
    }

    public void setmCategoryCode(String mCategoryCode) {
        this.mCategoryCode = mCategoryCode;
    }
}
