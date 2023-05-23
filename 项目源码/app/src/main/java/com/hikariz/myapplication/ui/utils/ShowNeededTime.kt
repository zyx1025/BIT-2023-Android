package com.hikariz.myapplication.ui.utils
import java.util.Calendar
import java.util.TimeZone
/*
本函数负责查找用户当前时间段需要查找的时间段
 */
val targetTime0 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
    set(Calendar.HOUR_OF_DAY, 8)
    set(Calendar.MINUTE, 0)
}
//第一大节下课时间：9：35
val targetTime1 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
    set(Calendar.HOUR_OF_DAY, 9)
    set(Calendar.MINUTE, 35)
}
//第二大节下课时间：12：20
val targetTime2 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
    set(Calendar.HOUR_OF_DAY, 12)
    set(Calendar.MINUTE, 20)
}
//第三大节下课时间：14：55
val targetTime3 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
    set(Calendar.HOUR_OF_DAY, 14)
    set(Calendar.MINUTE, 55)
}
//第四大节下课时间：17：40
val targetTime4 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
    set(Calendar.HOUR_OF_DAY, 17)
    set(Calendar.MINUTE, 40)
}
//第五大节下课时间：20：55
val targetTime5 = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai")).apply {
    set(Calendar.HOUR_OF_DAY, 20)
    set(Calendar.MINUTE, 55)
}
val listOfTime= listOf(targetTime0, targetTime1, targetTime2, targetTime3, targetTime4,targetTime5)
val startTime= listOf(1,3,6,8,11)

fun ShowNeededTime():Int{
    val now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))
    if(now.before(listOfTime[0])){
        return 1;   //当前时间早于8：00，那就显示第一大节没课的
    }
    for(i in 1 until listOfTime.size){
        // Array out of bound check
        if(i < listOfTime.size && i-1 >= 0){
            if(now.after(listOfTime[i-1]) && now.before(listOfTime[i])){
                return startTime[i-1];
                //例如i=2，说明当前在9：35-12：20的时间段，此时会返回第三节开始没课的教室
            }
        }
    }
    return 0    //此时在20：55之后，显然所有教室都没课
}

fun ShowCurTime():Int{
    val now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Shanghai"))
    if(now.before(listOfTime[0])){
        return 1;    //同上，当前时间早于8：00时就显示第一大节没课的
    }
    for(i in 1 until listOfTime.size){
        // Array out of bound check
        if(i < listOfTime.size && i-1 >= 0){
            if(now.after(listOfTime[i-1]) && now.before(listOfTime[i])){
                return i;
            }
        }
    }
    return 0    //此时在20：55之后，显然所有教室都没课
}