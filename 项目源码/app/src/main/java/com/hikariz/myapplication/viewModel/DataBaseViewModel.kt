package com.hikariz.myapplication.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.hikariz.myapplication.room.Database.*
import com.hikariz.myapplication.room.entity.ClassroomTypeData
import com.hikariz.myapplication.room.entity.CoursesData
import com.hikariz.myapplication.room.repository.DataRepository
import kotlinx.coroutines.flow.Flow


class DataBaseViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: DataRepository
    val allCoursesData: LiveData<List<CoursesData>>
    val week = MutableLiveData<Int>()
    val dayOfWeek = MutableLiveData<Int>()
    val startTime = MutableLiveData<Int>()
    val courses: MediatorLiveData<List<ClassroomTypeData>> = MediatorLiveData()

    init {
        val classroomTypeDao = MyDatabase.getDatabaseClassroom(application).classroomTypeDao()
        val coursesDao = MyDatabase.getDatabaseCourses(application).coursesDao()
        repository = DataRepository(classroomTypeDao, coursesDao)
        allCoursesData = coursesDao.getAll().asLiveData()

        courses.addSource(week) { updateCourses() }
        courses.addSource(dayOfWeek) { updateCourses() }
        courses.addSource(startTime) { updateCourses() }
    }

    private fun updateCourses() {
        if (week.value != null && dayOfWeek.value != null && startTime.value != null) {
            val newCourses = repository.getClassroomsNotInCourses(week.value!!, dayOfWeek.value!!, startTime.value!!).asLiveData()
            courses.addSource(newCourses) { newClassroomData ->
                courses.removeSource(newCourses)
                courses.value = newClassroomData
            }
        }
    }
}