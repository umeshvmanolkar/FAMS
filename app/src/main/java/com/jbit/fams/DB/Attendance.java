package com.jbit.fams.DB;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.annotation.NonNull;

import static androidx.room.ForeignKey.CASCADE;

/**
 * Created by eisaadil on 04/05/18.
 */
@Entity(primaryKeys = {"regNo", "courseId"})
public class Attendance {
    @ForeignKey(entity = Student.class, parentColumns = "regNo", childColumns = "regNo", onDelete = CASCADE)
    @NonNull
    public String regNo;

    @ForeignKey(entity = Course.class, parentColumns = "courseId", childColumns = "courseId", onDelete = CASCADE)
    @NonNull
    public String courseId;

    public int attendanceNumber;


    public Attendance(String regNo, String courseId, int attendanceNumber) {
        this.regNo = regNo;
        this.courseId = courseId;
        this.attendanceNumber = attendanceNumber;

    }
}
