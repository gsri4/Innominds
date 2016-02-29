package com.innominds.rsfb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.activeandroid.query.Select;
import com.innominds.rsfb.model.CategoryCodes;
import com.innominds.rsfb.model.FunctionalRecords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FunctionalArea extends AppCompatActivity  {



    private Spinner mFunctionalSpinner,mBusinessAreaSpinner;
    private Button mBtn_Reset, mBtn_Submit;
    List<String> functionalAreas = new ArrayList<String>();
    List<String> functionalBusinessType = new ArrayList<String>();
    private String selectedItem;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public static  final String PREFS_NAME = "Spinner Values";
    public static  final String PREFS_FAREA = "Functional Area";
    public static  final String PREFS_FAREA_Index = "Functional Area Index";
    public static  final String PREFS_BTYPE = "Business Type";
    public static  final String PREFS_BTYPE_Index = "Business Type Index";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functional_area);

        mFunctionalSpinner = (Spinner)findViewById(R.id.spinner_funArea);
        mBusinessAreaSpinner = (Spinner)findViewById(R.id.spinner_businessCode);
        mBtn_Submit = (Button)findViewById(R.id.btn_submit);
        mBtn_Reset = (Button)findViewById(R.id.btn_reset);




        //Used to get the functional area and adding it to array list
        List<FunctionalRecords> list = new Select(new String[]{"functionalarea"}).from(FunctionalRecords.class).execute();
        for(int i = 0; i < list.size(); i++) {
            FunctionalRecords functionalRecordsData = list.get(i);
            functionalAreas.add(functionalRecordsData.getmFunctionalArea());
        }

        //Removing duplication from functional area list
        Set<String> hs = new HashSet<>();
        hs.addAll(functionalAreas);
        functionalAreas.clear();
        functionalAreas.addAll(hs);
        Collections.sort(functionalAreas, String.CASE_INSENSITIVE_ORDER);

        //Adding default element
        functionalAreas.add(0,"Functional Area");


        //Setting spinner items with functional area
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, functionalAreas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFunctionalSpinner.setAdapter(dataAdapter);

        //Addind default element
        functionalBusinessType.add(0, "Business Document Type");
        setBusinessCodeData(functionalBusinessType);



        mBtn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if((!(mFunctionalSpinner.getSelectedItem().toString().equals("Functional Area")))&& mBusinessAreaSpinner.getSelectedItem() != null && mBusinessAreaSpinner != null){
                    if(!(mBusinessAreaSpinner.getSelectedItem().toString().equals("No Business Type available")))
                    {
                        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        editor = settings.edit(); //2

                        editor.putString(PREFS_FAREA, mFunctionalSpinner.getSelectedItem().toString());
                        editor.putInt(PREFS_FAREA_Index, mFunctionalSpinner.getSelectedItemPosition());
                        editor.putString(PREFS_BTYPE, mBusinessAreaSpinner.getSelectedItem().toString());
                        editor.putInt(PREFS_BTYPE_Index, mBusinessAreaSpinner.getSelectedItemPosition());
                        editor.commit();

                        Intent codeResultsIntent = new Intent(FunctionalArea.this, FunctionalCodeResults.class);
                        startActivity(codeResultsIntent);
                    }else{
                        Toast.makeText(FunctionalArea.this,"Not able to request data without Business Type ",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FunctionalArea.this,"Please select Functional Area and Business Type to continue",Toast.LENGTH_SHORT).show();
                }

            }
        });


        mBtn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFunctionalSpinner.setSelection(0);
                functionalBusinessType.add(0,"Business Document Type");
                setBusinessCodeData(functionalBusinessType);

            }
        });

        mFunctionalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position != 0){
                    //Get selected item from spinner
                    selectedItem = mFunctionalSpinner.getSelectedItem().toString();
                    functionalBusinessType.clear();
                    setCategoryCodeSpinnerData(selectedItem);
                 }else{
                    functionalBusinessType.clear();
                    functionalBusinessType.add(0,"Business Document Type");
                    setBusinessCodeData(functionalBusinessType);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mBusinessAreaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }




     //Used to add the elements to spinner basing on functional area and codes also sort the array list
    private void setCategoryCodeSpinnerData(String selectedElement){

        List<FunctionalRecords> selectedRecords = new Select().from(FunctionalRecords.class).where("functionalarea = ?", selectedElement).execute();

        for(int i = 0; i < selectedRecords.size(); i++) {
            FunctionalRecords functionalRecordsData = selectedRecords.get(i);


                    List<CategoryCodes> categoryCodes = new Select().from(CategoryCodes.class).where("categorycode = ?", functionalRecordsData.getmCategoryCode()).execute();
                    for(int j = 0; j < categoryCodes.size(); j++) {
                       CategoryCodes  categoryData = categoryCodes.get(j);
                        functionalBusinessType.add(categoryData.getmCategoryDocType());

                    }

        }
        if((functionalBusinessType != null) && !(functionalBusinessType.isEmpty())) {
            //Removing duplication from functional area list
            Set<String> hs = new HashSet<>();
            hs.addAll(functionalBusinessType);
            functionalBusinessType.clear();
            functionalBusinessType.addAll(hs);
            Collections.sort(functionalBusinessType, String.CASE_INSENSITIVE_ORDER);
        }else{
            functionalBusinessType.add("No Business Type available");
        }
        setBusinessCodeData(functionalBusinessType);

    }

    //used to set the spinner value of Business type
    private void setBusinessCodeData( List<String> codes){
        //Setting spinner items with functional area
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, codes);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBusinessAreaSpinner.setAdapter(dataAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
