package com.hikariz.myapplication.ui.screens

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.*
import com.hikariz.myapplication.ui.utils.*
import com.hikariz.myapplication.viewModel.DistanceViewModel
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material3.CircularProgressIndicator
import com.hikariz.myapplication.room.entity.ClassroomTypeData
import com.hikariz.myapplication.room.entity.CoursesData
import com.hikariz.myapplication.viewModel.DataBaseViewModel
import java.util.Calendar


@Composable
fun SearchFound(navController: NavController, distanceViewModel:DistanceViewModel,databaseViewModel:DataBaseViewModel) {
    val distanceB by distanceViewModel.distanceB.observeAsState(0.0)
    val distanceF by distanceViewModel.distanceF.observeAsState(0.0)
    val distanceG by distanceViewModel.distanceG.observeAsState(0.0)
    val distanceH by distanceViewModel.distanceH.observeAsState(0.0)
    val distanceI by distanceViewModel.distanceI.observeAsState(0.0)
    val distanceM by distanceViewModel.distanceM.observeAsState(0.0)
    val dataList by databaseViewModel.courses.observeAsState(emptyList<ClassroomTypeData>())
    val distanceMap = mapOf(
        "B" to distanceB,
        "F" to distanceF,
        "G" to distanceG,
        "H" to distanceH,
        "I" to distanceI,
        "M" to distanceM
    )

    val sortedDataList = dataList.sortedWith(Comparator { o1, o2 ->
        val distanceO1 = distanceMap[o1.id[0].toString()]
        val distanceO2 = distanceMap[o2.id[0].toString()]
        when {
            distanceO1 == null || distanceO2 == null -> 0
            distanceO1 < distanceO2 -> -1
            distanceO1 > distanceO2 -> 1
            else -> 0
        }
    })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if(sortedDataList.isEmpty()){
            CircularProgressIndicator(modifier = Modifier.padding(10.dp))
            //未查询完，显示转圈
        }else{
            Text(text = "查询到当前时间段有${sortedDataList.size}个教室没课")
            var cur= ShowCurTime()
            var note:String=when(cur){
                0->"现在所有教室都没课";
                else-> "注：当前显示的是第${cur}大节没有课的教室"
            }
            Text(text = note)
        }
        RememberTime(viewModel = databaseViewModel)
        LazyColumn {
            items(sortedDataList.size) { index ->
                ClassroomTypeCard(sortedDataList[index])
            }
        }
    }
}

@Composable
fun ClassroomTypeCard(classroomTypeData: ClassroomTypeData) {
    Card(
        modifier = Modifier
            .padding(bottom = 16.dp, start = 8.dp, end = 8.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ShowResult(classroomTypeData)
        }
    }
}



