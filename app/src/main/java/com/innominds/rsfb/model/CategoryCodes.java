package com.innominds.rsfb.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by sgajula on 24/02/16.
 */


@Table(name = "CategoryCodes")
public class CategoryCodes extends Model {


    @Column(name = "categorycode")
    public String mCategoryCode;

    @Column(name = "categorydoctype")
    public String mCategoryDocType;

    public CategoryCodes() {
        super();
    }

    public String getmCategoryCode() {
        return mCategoryCode;
    }

    public void setmCategoryCode(String mCategoryCode) {
        this.mCategoryCode = mCategoryCode;
    }

    public String getmCategoryDocType() {
        return mCategoryDocType;
    }

    public void setmCategoryDocType(String mCategoryDocType) {
        this.mCategoryDocType = mCategoryDocType;
    }
}
