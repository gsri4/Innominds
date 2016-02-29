package com.innominds.rsfb.excelreader;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.innominds.rsfb.MainActivity;
import com.innominds.rsfb.excelreader.ExcelSheetReader;

import java.io.InputStream;
import java.security.PrivateKey;


/**
 * Created by cgoru on 24/02/16.
 */
public class ExcelReaderTask extends AsyncTask<InputStream, Void, Void> {

    private Context mContext;
    private ProgressDialog pDialog;

    public ExcelReaderTask (Context context){
        mContext = context;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Application data  loading for the first time, it takes few minutes to load..");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
    }

    @Override
    protected Void doInBackground(InputStream... params) {
        ExcelSheetReader excelReader = new ExcelSheetReader();
        excelReader.parseExcel(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
            pDialog.dismiss();
    }
}
