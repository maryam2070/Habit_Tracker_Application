package com.example.habittrackerapplication.common

import java.text.SimpleDateFormat
import java.util.*


fun getDayName(): String {
    val calendar = Calendar.getInstance()
    val date = calendar.time
    return SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
}
fun getNumberOfDayInMonth(): Int {
    val cal = Calendar.getInstance()
    return cal[Calendar.DAY_OF_MONTH]
}
fun getFormattedDate(): String {
    var calendar = Calendar.getInstance()
    val inFormat = SimpleDateFormat("dd-MM-yyyy")
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    val date: Date = inFormat.parse(formatter.format(calendar.time))
    var outFormat = SimpleDateFormat("dd-MM-yyyy")
    return outFormat.format(date)
}