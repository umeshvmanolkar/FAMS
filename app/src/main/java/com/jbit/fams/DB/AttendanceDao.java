package com.jbit.fams.DB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

/**
 * Created by eisaadil on 04/05/18.
 */

@Dao
public interface AttendanceDao {



    @Query("SELECT * FROM Attendance where courseId = :courseId and regNo = :regNo LIMIT 1")
    Attendance getAttendance(String courseId, String regNo);

    @Query("UPDATE Attendance SET attendanceNumber = attendanceNumber + 1 WHERE courseId = :courseId and regNo = :regNo")
    void incrementAttendance(String courseId, String regNo);

    @Query("UPDATE Attendance SET attendanceNumber = attendanceNumber - 1 WHERE courseId = :courseId and regNo = :regNo")
    void decrementAttendance(String courseId, String regNo);

    @Query("UPDATE Attendance SET attendanceNumber = :attendanceNumber WHERE courseId = :courseId and regNo = :regNo")
    void setAttendance(String courseId, String regNo, int attendanceNumber);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Attendance... attendances);

    @Query("DELETE from Attendance where courseId=:courseId and regNo=:regNo")
    void deleteByStudentId(String courseId, String regNo);

}
