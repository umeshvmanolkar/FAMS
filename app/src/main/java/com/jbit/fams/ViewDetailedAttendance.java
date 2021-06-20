package com.jbit.fams;

import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jbit.fams.DB.Student;
import com.jbit.fams.Utilities.Utils;

import java.util.ArrayList;

public class ViewDetailedAttendance extends AppCompatActivity {


    // private AttendancePresentAdapter numbersArrayAdapter;
    private ListView numbersListView;
    private TextView tvNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_attendance);

        if (android.os.Build.VERSION.SDK_INT > 9){
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        //ab.setSubtitle("Click on the picture to keep adding students");
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setElevation(100);
        int id = getIntent().getExtras().getInt("id");

        ArrayList<String> dataList = new ArrayList<>();
        for(int i=0;i<Utils.getList().size();i++){
            dataList.add(Utils.getList().get(i).studentName);
        }
        Spinner spStudentList = findViewById(R.id.spStudentList);

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>
                (this, R.layout.spinner_item,
                        dataList); //selected item will look like a spinner set from XML

        spStudentList.setAdapter(spinnerArrayAdapter);
        numbersListView = findViewById(R.id.listView);
        spStudentList.setSelection(id);


        /*Student student = Utils.getList().get(0);
        updateStudentAttendanace(student.courseId,student.studentId);
*/

        final TextView  tvRegNumber = findViewById(R.id.tvRegNumber);

        tvNoData = findViewById(R.id.tvNoData);
        spStudentList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                Student student = Utils.getList().get(position);
                tvRegNumber.setText("Registration Number -"+student.regNo);
                updateStudentAttendanace(student.courseId,student.studentId);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });











    }


    private  void updateStudentAttendanace(String courseId,String studentId){

        String listOfDates = Utils.getDatesListbystudentCourseandstID(courseId,studentId);


        if(listOfDates!=null){


            String [] list = listOfDates.split("\\+");
            if(list!=null){
                if(!list[0].isEmpty()){
                    tvNoData.setVisibility(View.GONE);
                    numbersListView.setVisibility(View.VISIBLE);
                    AttendancePresentAdapter        numbersArrayAdapter = new AttendancePresentAdapter(this, list);
                    numbersListView.setAdapter(numbersArrayAdapter);
                }else{

                    tvNoData.setVisibility(View.VISIBLE);
                    numbersListView.setVisibility(View.GONE);
                }

            }
            // if(numbersArrayAdapter==null) {

            /*}else{

                numbersArrayAdapter.updateAdapter(list);
            }*/

        }
    }


}
