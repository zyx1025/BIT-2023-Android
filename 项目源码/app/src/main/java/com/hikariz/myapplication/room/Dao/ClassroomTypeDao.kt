package com.hikariz.myapplication.room.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import com.hikariz.myapplication.room.entity.ClassroomTypeData
import com.hikariz.myapplication.room.entity.CoursesData

@Dao
interface ClassroomTypeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(classroomTypeData: ClassroomTypeData)

    @Query("select * from classroomType")
    fun getAll(): Flow<List<ClassroomTypeData>>

    @Query("SELECT * FROM classroomType WHERE id NOT IN (:ids)")
    fun getClassroomsNotInIds(ids: List<String>): Flow<List<ClassroomTypeData>>
}



//    @Query("select * from mydata where id=:id")
//    suspend fun findById(id: Int): MyData