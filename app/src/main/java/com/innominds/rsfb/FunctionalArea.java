package com.innominds.rsfb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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



    private AutoCompleteTextView mFunctionalAutoView,mBusinessAreaAutoView;
    private Button mBtn_Reset, mBtn_Submit;
    List<String> functionalAreas = new ArrayList<String>();
    List<String> functionalBusinessType = new ArrayList<String>();
    private String selectedItem;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    public static  final String PREFS_NAME = "Spinner Values";
    public static  final String PREFS_FAREA = "Functional Area";
    public static  final String PREFS_BTYPE = "Business Type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_functional_area);

        mFunctionalAutoView = (AutoCompleteTextView)findViewById(R.id.fun_txtview);
        mBusinessAreaAutoView = (AutoCompleteTextView)findViewById(R.id.btype_txtview);
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

        //Adding default element to Functional Area autocomplete view
         setFunctionalAreaData(functionalAreas);




        //Adding default element Business Type
        setBusinessCodeData(functionalBusinessType);



        //Checking weather elements are empty or matches with any of stings.
        mBtn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!(mFunctionalAutoView.getText().toString().isEmpty()) && !(mBusinessAreaAutoView.getText().toString().isEmpty()) &&
                        (!(mFunctionalAutoView.getText().toString().equals("Functional Area"))) && !(mBusinessAreaAutoView.getText().toString().equals("Business Document Type"))) {
                    if (!(mBusinessAreaAutoView.getText().toString().equals("No Business Type available"))) {
                        settings = getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                        editor = settings.edit(); //2

                        editor.putString(PREFS_FAREA, mFunctionalAutoView.getText().toString());
                        editor.putString(PREFS_BTYPE, mBusinessAreaAutoView.getText().toString());
                        editor.commit();

                        Intent codeResultsIntent = new Intent(FunctionalArea.this, FunctionalCodeResults.class);
                        startActivity(codeResultsIntent);
                    } else {
                        Toast.makeText(FunctionalArea.this, "Not able to request data without Business Type ", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FunctionalArea.this, "Please select Functional Area and Business Type to continue", Toast.LENGTH_SHORT).show();
                }

            }
        });



        //When ever user selected particular item in mFunctionalAutoView then we dismiss keyboard and shows default hints
        mFunctionalAutoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get selected item from spinner
                hideSoftKeyBoard();
                selectedItem = mFunctionalAutoView.getText().toString();
                functionalBusinessType.clear();
                setCategoryCodeSpinnerData(selectedItem);
                mBusinessAreaAutoView.getText().clear();
                mBusinessAreaAutoView.setHint(getResources().getString(R.string.str_buss_doctype));
                mBusinessAreaAutoView.requestFocus();

            }
        });





        //Hiding keyboard when mBusinessAreaAutoView item selected
        mBusinessAreaAutoView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                hideSoftKeyBoard();
            }
        });


        //Text changed in mFunctionalAutoView area then we again set the adapter data
        mFunctionalAutoView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                selectedItem = mFunctionalAutoView.getText().toString();
                functionalBusinessType.clear();
                setCategoryCodeSpinnerData(selectedItem);

            }
        });



        //When mFunctionalAutoView clicked then it shows dropdown with adapter data
        mFunctionalAutoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFunctionalAutoView.showDropDown();
            }
        });


        //When mBusinessAreaAutoView clicked then it shows dropdown with adapter data
        mBusinessAreaAutoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBusinessAreaAutoView.showDropDown();
            }
        });



        //Reset both the autocomplete views data
        mBtn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFunctionalAutoView.setText(" ");
                mFunctionalAutoView.getText().clear();
                mFunctionalAutoView.setHint(getResources().getString(R.string.str_funArea));
                setFunctionalAreaData(functionalAreas);
                mFunctionalAutoView.setSelection(0);
                mBusinessAreaAutoView.setText(" ");
                mBusinessAreaAutoView.getText().clear();
                mBusinessAreaAutoView.setHint(getResources().getString(R.string.str_buss_doctype));
                mFunctionalAutoView.requestFocus();
            }
        });

    }


    //Check and dismiss soft keyboard
    private void hideSoftKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        if(imm.isAcceptingText()) { // verify if the soft keyboard is open
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
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
        mBusinessAreaAutoView.setAdapter(dataAdapter);

    }

    //used to set the spinner value of Functional Area
    private void setFunctionalAreaData( List<String> functionalAreas){
        //Setting spinner items with functional area
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, functionalAreas);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFunctionalAutoView.setAdapter(dataAdapter);

    }


    //Clicked on on back pressed then app will exit
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
