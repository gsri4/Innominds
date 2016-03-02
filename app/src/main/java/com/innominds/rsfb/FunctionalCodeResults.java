package com.innominds.rsfb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.innominds.rsfb.model.CategoryCodes;
import com.innominds.rsfb.model.FunctionalRecords;

public class  FunctionalCodeResults extends AppCompatActivity {


    private TextView txtFunctionalArea, txtBusinessType, txtRetentionSchedule_value,
            txtVital_value,txtCategory_Value;
    private Button btn_LookupAnother, btn_Close;
    private String strBusinessDocType,strFunctionalArea;
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functional_code_results);
        mToolbar = (Toolbar) findViewById(R.id.results_toolbar);
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }


        txtFunctionalArea = (TextView)findViewById(R.id.txt_funtionalArea);
        txtBusinessType = (TextView)findViewById(R.id.txt_doctype);
        txtRetentionSchedule_value = (TextView)findViewById(R.id.txt_schedule_value);
        txtVital_value = (TextView)findViewById(R.id.txt_vital_value);
        txtCategory_Value = (TextView)findViewById(R.id.txt_catg_value);
        btn_LookupAnother = (Button)findViewById(R.id.btn_lookup_another);
        btn_Close = (Button)findViewById(R.id.btn_close);

        SharedPreferences settings;
        settings = getApplicationContext().getSharedPreferences(FunctionalArea.PREFS_NAME, Context.MODE_PRIVATE); //1
        strFunctionalArea = settings.getString(FunctionalArea.PREFS_FAREA, null); //2
        strBusinessDocType = settings.getString(FunctionalArea.PREFS_BTYPE, null); //2

        txtFunctionalArea.setText(strFunctionalArea);
        txtBusinessType.setText(strBusinessDocType);

        CategoryCodes categoryCodes = new Select().from(CategoryCodes.class).where("categorydoctype = ?", strBusinessDocType).executeSingle();


        //Handling exceptions when category data returns null
        try {
            //Check wather category was empty and query to get the value of vital and retentation
            if (!(categoryCodes.getmCategoryCode().isEmpty()) && categoryCodes != null) {
                txtCategory_Value.setText(categoryCodes.getmCategoryCode());
                FunctionalRecords selectedRecord = new Select().from(FunctionalRecords.class).where("categorycode = ?", categoryCodes.getmCategoryCode()).executeSingle();
                txtVital_value.setText(selectedRecord.getmRecVital());
                txtRetentionSchedule_value.setText(selectedRecord.getmRecValue());
            } else {
                Toast.makeText(getApplicationContext(), "Entered incorrect Data", Toast.LENGTH_SHORT).show();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            Toast.makeText(FunctionalCodeResults.this, "Entered incorrect Data", Toast.LENGTH_SHORT).show();
        }


        //Finishing present activity and showing FunctionalCodeResults activity
        btn_LookupAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent functionalCodeIntent = new Intent(FunctionalCodeResults.this, FunctionalArea.class);
                functionalCodeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(functionalCodeIntent);
                finish();
            }
        });


        //Finishing present activity and showing Main activity
        btn_Close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainActivityIntent = new Intent(FunctionalCodeResults.this, MainActivity.class);
                mainActivityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainActivityIntent);
                finish();
            }
        });



    }


    //Clicking on back button on action bar will  kills current activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
