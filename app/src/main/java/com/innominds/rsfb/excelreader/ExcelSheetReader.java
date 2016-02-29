package com.innominds.rsfb.excelreader;

import android.app.ProgressDialog;
import android.util.Log;

import com.innominds.rsfb.MainActivity;
import com.innominds.rsfb.model.CategoryCodes;
import com.innominds.rsfb.model.FunctionalRecords;

import java.io.IOException;
import java.io.InputStream;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExcelSheetReader {
    private static final String TAG = "ExcelReader";

    public void parseExcel(InputStream inputStream) {
        Workbook workbook = null;
        try {
            workbook = Workbook.getWorkbook(inputStream);
            Sheet[] sheets = workbook.getSheets();
                parseSheet(sheets[0]);
                parseCategorySheet(sheets[1]);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } finally {
            //close the workbook
            assert workbook != null;
            workbook.close();
        }
    }

    private void parseSheet(Sheet sheet) {

        assert sheet != null;
        int count = 0;
        String cellData = "Not Applicable"; // For rep empty record
        Log.d(TAG, "printing records for sheet "+ sheet.getName());
        for(int i = 1; i < sheet.getRows(); i++) {
            count++;
            //Creating instance for FunctionalRecords pojo class to store all records in sheet1
            FunctionalRecords functionalRecords = new FunctionalRecords();

            //Creating stringBuilder to store entire record
            StringBuilder stringBuilder = new StringBuilder();
            for(int j = 0; j < sheet.getColumns(); j++) {
                //Checking weather cell data was there or not. If it is empty then we store it as "NoData"
                if(sheet.getCell(j, i).getContents().isEmpty()){
                    stringBuilder.append(cellData);
                }else{
                    stringBuilder.append((sheet.getCell(j, i).getContents()));
                }
                stringBuilder.append(":");
            }
            Log.d(TAG, "record " + count + " is " + stringBuilder.toString());

            //Split stringBuilder with ":" separated
            String[] separated = stringBuilder.toString().split(":");

            //Saving records into database
            functionalRecords.setmFunctionalArea(separated[0]);
            functionalRecords.setmCategoryCode(separated[1]);
            functionalRecords.setmRecValue(separated[2]);
            functionalRecords.setmRecVital(separated[3]);
            functionalRecords.save();

        }
    }




    private void parseCategorySheet(Sheet sheet) {
        assert sheet != null;
        int count = 0;
        String cellData = "Not Applicable"; // For rep empty record
        Log.d(TAG, "printing records for sheet "+ sheet.getName());
        for(int i = 0; i < sheet.getRows(); i++) {
            count++;
            //Creating instance for FunctionalRecords pojo class to store all records in sheet1
            CategoryCodes categoryCodes = new CategoryCodes();

            //Creating stringBuilder to store entire record
            StringBuilder stringBuilder = new StringBuilder();
            for(int j = 0; j < sheet.getColumns(); j++) {
                //Checking weather cell data was there or not. If it is empty then we store it as "NoData"
                if(sheet.getCell(j, i).getContents().isEmpty()){
                    stringBuilder.append(cellData);
                }else{
                    stringBuilder.append((sheet.getCell(j, i).getContents()));
                }
                stringBuilder.append(":");
            }
            Log.d(TAG, "record " + count + " is " + stringBuilder.toString());

            //Split stringBuilder with ":" separated
            String[] separated = stringBuilder.toString().split(":");

            //Saving records into database
            categoryCodes.setmCategoryCode(separated[0]);
            categoryCodes.setmCategoryDocType(separated[1]);
            categoryCodes.save();

        }
    }
}
