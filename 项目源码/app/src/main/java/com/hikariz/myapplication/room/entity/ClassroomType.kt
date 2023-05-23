package com.hikariz.myapplication.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
记录教室类型的表，查找到空闲教室后，通过这个表得知其类型
两个属性，第一个属性为id，如F204，第二个属性为教室类型
type=1：小教室（约容纳20-30人）；type=2：大阶梯教室；type=3：大教室（约容纳100人）
 */
@Entity(tableName = "classroomType")
data class ClassroomTypeData(
    @PrimaryKey
    val id: String,
    val type: Int
)
