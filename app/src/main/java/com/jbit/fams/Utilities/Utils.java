package com.jbit.fams.Utilities;

import com.jbit.fams.DB.Student;
import com.pixplicity.easyprefs.library.Prefs;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Ramakrishna on 07-Jun-21.
 */

public class Utils {

    public static String getDate(){

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());

        return formattedDate;
    }


    public static void updatestudentAttendance(String courseId,String studentId){

        //String value=  Prefs.getString(courseId+"+"+ studentId, Utils.getDate());
        String value=  Prefs.getString(courseId+"+"+ studentId, "");
        String latestData=value;
        if(!value.contains(Utils.getDate())){
            latestData =  Utils.getDate()+"+"+value;
        }
        Prefs.putString(courseId+"+"+ studentId, latestData);


    }


    public static String getDatesListbystudentCourseandstID(String courseId,String studentId){

        //String value=  Prefs.getString(courseId+"+"+ studentId, Utils.getDate());
        String value=  Prefs.getString(courseId+"+"+ studentId, "");

        return value;

    }
   private static List<Student> studentsList;
    public static List<Student> getList(){


        return studentsList;
    }

    public static void setStudentsList(List<Student> studentsData){

        studentsList=studentsData;
    }
}
