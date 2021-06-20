package com.jbit.fams;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.legacy.app.FragmentCompat;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jbit.fams.DB.AppDatabase;
import com.jbit.fams.DB.Attendance;
import com.jbit.fams.DB.Student;
import com.jbit.fams.Utilities.Utils;
import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAttendance extends Fragment {

    ListView viewAttendanceList;
    StudentListAdapter studentListAdapter;
    private EditText ivSearch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.content_view_attendance,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        viewAttendanceList = view.findViewById(R.id.viewAttendanceList);

        ivSearch = (EditText)view.findViewById(R.id.ivSearch);
        ivSearch.setVisibility(View.GONE);
//        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

       ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivSearch.requestFocus();
                ivSearch.setFocusable(true);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

            }
        });

        ivSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int start, int before, int count) {
                // TODO Auto-generated method stub
                //MainActivity.this.adapt.getFilter().filter(s);
                int textlength = cs.length();
                ArrayList<Student> tempArrayList = new ArrayList<Student>();
                if(allStudents==null)
                    return;
                for(Student c: allStudents){
                    if (textlength <= c.studentName.length()) {
                        if (c.studentName.toLowerCase().contains(cs.toString().toLowerCase())) {
                            tempArrayList.add(c);
                        }
                    }
                }
                studentListAdapter = new StudentListAdapter(getActivity(), R.layout.list_view_students_row, tempArrayList);
                viewAttendanceList.setAdapter(studentListAdapter);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });


    }
    List<Student> allStudents;

    public void getAllStudents(){
        AppDatabase db = AppDatabase.getAppDatabase(getActivity());
         allStudents = db.studentDao().getAllByCourseId(Prefs.getString("courseId", ""));



        if(allStudents.isEmpty()){
            viewAttendanceList.setVisibility(View.GONE);
            (getActivity().findViewById(R.id.noStudentsFoundText)).setVisibility(View.VISIBLE);
            (getActivity().findViewById(R.id.takeAttendance)).setVisibility(View.GONE);

        }
        else{
            (getActivity().findViewById(R.id.noStudentsFoundText)).setVisibility(View.GONE);
            (getActivity().findViewById(R.id.takeAttendance)).setVisibility(View.VISIBLE);
        }

        Collections.sort(allStudents, new Comparator<Student>() {
            @Override
            public int compare(Student t1, Student t2) {
                return (t1.studentName).compareTo(t2.studentName);
            }
        });


        Utils.setStudentsList(allStudents);
        if(allStudents!=null&&allStudents.size()>0){
            ivSearch.setVisibility(View.VISIBLE);
        }else{
            ivSearch.setVisibility(View.GONE);
        }
        studentListAdapter = new StudentListAdapter(getActivity(), R.layout.list_view_students_row, allStudents);
        viewAttendanceList.setAdapter(studentListAdapter);
    }

    @Override
    public void onStart() {
        if (alreadyHasPermission()){
            getAllStudents();
        }
        else{
            FragmentCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
        super.onStart();
    }

    private boolean alreadyHasPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    onStart();
                } else {
                    Toast.makeText(getActivity(), "Permission denied to read your External storage", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public class StudentListAdapter extends ArrayAdapter<Student>  implements Filterable {

        private Context context;

        public StudentListAdapter(Activity context, int resource, List<Student> students) {
            super(context, resource, students);
            this.context = context;
        }


        @Override
        public long getItemId(int i) {
            return 0;
        }


        @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
        public View getView(final int position, View convertView, ViewGroup parent) {

            final Student student = getItem(position);

            if (convertView == null) {

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_view_students_row, parent, false);
            }


            TextView studentName = convertView.findViewById(R.id.studentName);
            CircleImageView studentFaceImage = convertView.findViewById(R.id.studentFaceImage);
            TextView studentRegNo = convertView.findViewById(R.id.studentRegNo);

            final TextView attendanceText = convertView.findViewById(R.id.attendanceText);
            TextView maxAttendanceText = convertView.findViewById(R.id.maxAttendanceText);

            convertView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
                @Override
                public void onClick(View v) {
                   Intent intent = new Intent(getActivity(),ViewDetailedAttendance.class);
                   intent.putExtra("id",position);
                   getActivity().startActivity(intent);
                }
            });

            assert student != null;
            studentName.setText(student.studentName);
            studentRegNo.setText(student.regNo);

            AppDatabase db = AppDatabase.getAppDatabase(getActivity());

            if (db.attendanceDao().getAttendance(student.courseId, student.regNo)==null){
                db.attendanceDao().insertAll(new Attendance(student.regNo, student.courseId, 0));
            }


            final int attendanceNumber = db.attendanceDao().getAttendance(student.courseId, student.regNo).attendanceNumber;
            int maxAttendance = db.courseDao().getNumberOfClasses(student.courseId);

            attendanceText.setText(""+attendanceNumber);
            maxAttendanceText.setText("/"+maxAttendance);

            String[] faceIDs = (new Gson()).fromJson(student.faceArrayJson, String[].class);

            if (faceIDs.length != 0) {

                String photoPath = Environment.getExternalStorageDirectory() + "/Faces/" + faceIDs[0] + ".jpg"; //take first faceId image /storage/emulated/0/7a677caf-1ece-47af-a771-857f979cd241.jpg

                if (!(new File(photoPath).exists())){
                    studentFaceImage.setImageResource(R.drawable.person_icon);
                }
                else{
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    final Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
                    studentFaceImage.setImageBitmap(bitmap);
                }


            }

            return convertView;

        }



    }
}
