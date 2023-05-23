package com.hikariz.myapplication.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hikariz.myapplication.R
import com.hikariz.myapplication.room.entity.ClassroomTypeData

/*
在数据库查询完所有满足条件的教室后，针对每条记录生成相同格式的ui
*/

@Composable
fun ShowResult(classroomTypeData: ClassroomTypeData){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column() {
            Text(text = "${classroomTypeData.id}", style = MaterialTheme.typography.h4)
            ShowClassroomText(type = classroomTypeData.type)
        }
        ShowClassroomImage(type = classroomTypeData.type)
    }
}

/*
根据教室的不同类型，显示不同的照片
type=1：小教室（约容纳20-30人）；type=2：大阶梯教室；type=3：大教室（约容纳100人）
 */
@Composable
fun ShowClassroomText(type: Int) {
    val introduction = when (type) {
        1 -> "教室"
        2 -> "阶梯教室"
        else -> "大教室"
    }
    Text(
        text = introduction,
        color = Color.DarkGray,
        fontSize = 18.sp,
        fontFamily = FontFamily.Serif
    )
}

@Composable
fun ShowClassroomImage(type: Int) {
    val imageResource = when (type) {
        1 -> R.drawable.type_1
        2 -> R.drawable.type_2
        else -> R.drawable.type_3
    }
    Image(painterResource(id = imageResource),
        contentDescription = "",
        modifier = Modifier.size(75.dp)
    )
}