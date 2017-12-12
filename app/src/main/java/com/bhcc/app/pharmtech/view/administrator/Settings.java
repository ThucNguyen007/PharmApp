package com.bhcc.app.pharmtech.view.administrator;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.bhcc.app.pharmtech.R;
import com.bhcc.app.pharmtech.view.study.EditDatabase;

/**
 * Created by Senay tekle on 12/11/2017.
 */

public class Settings extends Fragment {


    EditDatabase pharmAppAdminDatabase;

    private Button DisplayBtn;
    public Button mInsertBtn;
    private Button DeleteBtn;
    private Button UpdateBtn;

    private EditText CodeEntrySpace;
    private EditText BrandEntry;
    private EditText GenericEntry;
    private EditText PurposeEntry;
    private EditText DeaScheduleEntry;
    private EditText SpecialEntry;
    private EditText CategoryEntry;
    private EditText StudyTopicEntry;

    String genericName,brandName,purpose,deaSch,special,category,studyTopic,id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout, container, false);

        pharmAppAdminDatabase = new EditDatabase(getActivity());

        DisplayBtn = (Button)view.findViewById(R.id.DisplayDataBtn);
        DeleteBtn = (Button)view.findViewById(R.id.DeleteData);
        UpdateBtn = (Button)view.findViewById(R.id.UpdateData);
        mInsertBtn = (Button)view.findViewById(R.id.input);

        CodeEntrySpace = (EditText) view.findViewById(R.id.CodeEntry);
        GenericEntry = (EditText) view.findViewById(R.id.GENERIC_NAME);
        BrandEntry = (EditText) view.findViewById(R.id.BRAND_NAME);
        PurposeEntry = (EditText) view.findViewById(R.id.PURPOSE);
        DeaScheduleEntry = (EditText) view.findViewById(R.id.DEA_SCH);
        SpecialEntry = (EditText) view.findViewById(R.id.SPECIAL);
        CategoryEntry = (EditText) view.findViewById(R.id.CATEGORY);
        StudyTopicEntry = (EditText) view.findViewById(R.id.STUDY_TOPIC);

        updateDataBase();
        displayDataBase();
        insertData();
        deleteDataBase();
        return view;
    }


    public void displayDataBase(){

        DisplayBtn.setOnClickListener(new View.OnClickListener() {//dispaly what is in database
            @Override
            public void onClick(View view) {
                Cursor resource = pharmAppAdminDatabase.displayData();

                if(resource.getCount() == 0){
                    alertMessage(getString(R.string.error), getString(R.string.no_data_in_database));
                    Toast.makeText(getActivity(),getString(R.string.no_data_in_database), Toast.LENGTH_LONG).show();
                }
                StringBuffer buffer = new StringBuffer();

                while(resource.moveToNext()){//DATA BASE WILL BE APPENDED HERE TO APPROPRIATE NAME

                    buffer.append(getString(R.string.reference_id_number) + "["+resource.getString(0)+"]\n");
                    buffer.append(getString(R.string.generic_name) + "["+resource.getString(1)+"]\n");
                    buffer.append(getString(R.string.brand_name) + "["+resource.getString(2)+"]\n");
                    buffer.append(getString(R.string.purpose) + "["+resource.getString(3)+"]\n");
                    buffer.append(getString(R.string.dea_sch) + "["+resource.getString(4)+"]\n");
                    buffer.append(getString(R.string.special) + "["+resource.getString(5)+"]\n");
                    buffer.append(getString(R.string.category) + "["+resource.getString(6)+"]\n");
                    buffer.append(getString(R.string.study_topic) + "["+resource.getString(7)+"]\n\n");

                }
                alertMessage(getString(R.string.database_title_show), buffer.toString());//ALERT MESSAGE
            }
        });
    }//end of display database

    public void alertMessage(String title, String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }//END OF ALERT
    public void insertData(){

        mInsertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean isInserted = pharmAppAdminDatabase.insertToDataBase(GenericEntry.getText().toString(),
                        BrandEntry.getText().toString(),PurposeEntry.getText().toString(),DeaScheduleEntry.getText().toString(),
                        SpecialEntry.getText().toString(),CategoryEntry.getText().toString(),StudyTopicEntry.getText().toString());//this all data inserted to database

                if(isInserted == true){
                    Toast.makeText(getActivity(), getString(R.string.drug_add_database_success),
                            Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(), getString(R.string.drug_add_database_failure),Toast.LENGTH_LONG).show();
                }

                GenericEntry.getText().clear();BrandEntry.getText().clear();PurposeEntry.getText().clear();
                DeaScheduleEntry.getText().clear();SpecialEntry.getText().clear();CategoryEntry.getText().clear();
                StudyTopicEntry.getText().clear();

            }


        });


    }
    public void deleteDataBase(){


        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Integer delete = pharmAppAdminDatabase.deleteData(CodeEntrySpace.getText().toString());

                if(delete > 0){
                    Toast.makeText(getActivity(), getString(R.string.drug_deleted_success), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getActivity(),getString(R.string.drug_deleted_failure),
                            Toast.LENGTH_LONG).show();
                }
            }
        });
        CodeEntrySpace.getText().clear();
    }//END OF DELETE

    public void updateDataBase(){

        UpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean update = pharmAppAdminDatabase.updateDataBase(String.valueOf(genericName),String.valueOf(brandName),
                        String.valueOf(purpose),String.valueOf(deaSch),String.valueOf(special),
                        String.valueOf(category),String.valueOf(studyTopic),String.valueOf(id));//this all data inserted to database
                String check = CodeEntrySpace.getText().toString();
                if(check.length() <= 0){
                    Toast.makeText(getActivity(),getString(R.string.id_required),Toast.LENGTH_LONG).show();

                }else {

                    if (update == true) {

                        Toast.makeText(getActivity(), getString(R.string.data_update_success), Toast.LENGTH_LONG).show();
                    } else {

                        Toast.makeText(getActivity(), getString(R.string.data_update_failure), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        CodeEntrySpace.getText().clear();
    }//end of update db

}
