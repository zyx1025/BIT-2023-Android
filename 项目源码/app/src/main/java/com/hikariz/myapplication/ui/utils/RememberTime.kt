package com.hikariz.myapplication.ui.utils

import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.hikariz.myapplication.viewModel.DataBaseViewModel
import kotlinx.coroutines.delay
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun RememberTime(viewModel: DataBaseViewModel) {
    val sdf = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone("Asia/Shanghai")

    val sdfDate = SimpleDateFormat("yyyy. MM.dd", Locale.getDefault())
    val currentTime = remember { mutableStateOf(sdf.format(Date())) }
    val calendar = remember { Calendar.getInstance().apply { firstDayOfWeek = Calendar.MONDAY } }

//本学期第一周周一是2023.2.20，新的学期需要修改这一数据
    val startDate = Calendar.getInstance().apply { time = sdfDate.parse("2023.2.20")!! }

    var daysDiff = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            val now = Date()
            currentTime.value = sdf.format(now)
            calendar.time = now
            daysDiff.value = ((calendar.timeInMillis - startDate.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()
            viewModel.week.value = 1 + daysDiff.value / 7
            viewModel.dayOfWeek.value = calendar.get(Calendar.DAY_OF_WEEK) - 1
            viewModel.startTime.value = ShowNeededTime()
            delay(1000L)
        }
    }
}