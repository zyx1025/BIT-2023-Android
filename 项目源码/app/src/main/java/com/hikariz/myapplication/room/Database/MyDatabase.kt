package com.hikariz.myapplication.room.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hikariz.myapplication.room.Dao.ClassroomTypeDao
import com.hikariz.myapplication.room.entity.ClassroomTypeData
import android.content.Context
import androidx.room.Room
import com.hikariz.myapplication.room.Dao.CoursesDao
import com.hikariz.myapplication.room.entity.CoursesData


@Database(
    entities = [ClassroomTypeData::class,CoursesData::class],
    version = 4,
    exportSchema=false	//不导出Schema
)
abstract class MyDatabase : RoomDatabase() {
    abstract fun classroomTypeDao(): ClassroomTypeDao
    abstract fun coursesDao(): CoursesDao

    companion object {
        @Volatile
        private var INSTANCE_CLASSROOM: MyDatabase? = null
        @Volatile
        private var INSTANCE_COURSES: MyDatabase? = null

        fun getDatabaseClassroom(context: Context): MyDatabase {
            return INSTANCE_CLASSROOM ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java, "classroomType.db"
                )
                    .createFromAsset("classroomType.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE_CLASSROOM = instance
                instance
            }
        }

        fun getDatabaseCourses(context: Context): MyDatabase {
            return INSTANCE_COURSES ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MyDatabase::class.java, "PreprocessedCourses.db"
                )
                    .createFromAsset("PreprocessedCourses.db")
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE_COURSES = instance
                instance
            }
        }
    }
}


