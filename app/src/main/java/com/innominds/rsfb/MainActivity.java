package com.innominds.rsfb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.innominds.rsfb.excelreader.ExcelReaderTask;
import com.innominds.rsfb.model.FunctionalRecords;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button btn_Lookup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        btn_Lookup =(Button)findViewById(R.id.btn_LookUp);

        //Check excel  data was present or not
        //If not read the content form excel sheet
        List<FunctionalRecords> functionalRecords = new Select().from(FunctionalRecords.class).execute();
        if(functionalRecords.isEmpty() ) {
            readExcelData("RSBD.xls");
        }

        btn_Lookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent areaSelectionIntent = new Intent(MainActivity.this,FunctionalArea.class);
                startActivity(areaSelectionIntent);
                finish();

            }
        });

    }

    //Used to read the excel data
    private void readExcelData(String fileName) {
        try {
            new ExcelReaderTask(MainActivity.this).execute(getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error opening the asset file.",
                    Toast.LENGTH_LONG).show();
            finish();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
