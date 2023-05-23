package com.hikariz.myapplication.room.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.hikariz.myapplication.room.Dao.ClassroomTypeDao
import com.hikariz.myapplication.room.Dao.CoursesDao
import com.hikariz.myapplication.room.Database.MyDatabase
import com.hikariz.myapplication.room.entity.ClassroomTypeData
import com.hikariz.myapplication.room.entity.CoursesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest


class DataRepository(
    private val classroomTypeDao: ClassroomTypeDao,
    private val coursesDao: CoursesDao
) {
    fun getClassroomsNotInCourses(week: Int, dayOfWeek: Int, startTime: Int): Flow<List<ClassroomTypeData>> {
        return coursesDao.getCourses(week, dayOfWeek, startTime)
            .flatMapLatest { classroomIds ->
                classroomTypeDao.getClassroomsNotInIds(classroomIds)
            }
    }
}



//调试用方法：显示当前时间段所有有课的教室
//    fun getCourses(week: Int, dayOfWeek: Int, startTime: Int): Flow<List<CoursesData>> {
//        return coursesDao.getCourses(week, dayOfWeek, startTime)
//    }