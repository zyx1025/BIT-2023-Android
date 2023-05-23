package com.hikariz.myapplication.room.entity

import androidx.room.ColumnInfo
import androidx.room.DatabaseView
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.DayOfWeek

/*
    记录课程信息的表，通过这个表来实现查找空闲教室
 */

@Entity(tableName = "PreprocessedCourses")
data class CoursesData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "week")
    val week: Int,

    @ColumnInfo(name = "day_of_week")
    val day_of_week: Int,

    @ColumnInfo(name = "start_time")
    val start_time: Int,

    @ColumnInfo(name = "end_time")
    val end_time: Int,

    @ColumnInfo(name = "classroom")
    val classroom: String,

    @ColumnInfo(name = "is_rescheduled")
    val is_rescheduled: Int?,
)
