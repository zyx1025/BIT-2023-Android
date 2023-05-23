package com.hikariz.myapplication.room.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hikariz.myapplication.room.entity.ClassroomTypeData
import com.hikariz.myapplication.room.entity.CoursesData
import kotlinx.coroutines.flow.Flow

@Dao
interface CoursesDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(CoursesData: CoursesData)

    @Query("select * from PreprocessedCourses")
    fun getAll(): Flow<List<CoursesData>>

    @Query("SELECT classroom FROM PreprocessedCourses WHERE week = :week AND day_of_week = :dayOfWeek AND start_time = :startTime AND is_rescheduled = 0")
    fun getCourses(week: Int,  dayOfWeek: Int,startTime: Int): Flow<List<String>>
}
