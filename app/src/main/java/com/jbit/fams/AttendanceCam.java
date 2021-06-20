package com.jbit.fams;

import android.app.Application;

public class AttendanceCam extends Application {
    public String courseId;


    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }
}
