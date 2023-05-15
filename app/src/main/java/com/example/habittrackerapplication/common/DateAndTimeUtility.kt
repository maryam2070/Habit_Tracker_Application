package com.example.habittrackerapplication.common

import java.text.SimpleDateFormat
import java.util.*


fun getDayName(calendar: Calendar): String {
    val date = calendar.time
    return SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.time)
}
fun getNumberOfDayInMonth(calendar: Calendar): Int {
    return calendar[Calendar.DAY_OF_MONTH]
}
fun getFormattedDate(calendar: Calendar): String {
    val inFormat = SimpleDateFormat("dd-MM-yyyy")
    val formatter = SimpleDateFormat("dd-MM-yyyy")
    val date: Date = inFormat.parse(formatter.format(calendar.time))
    var outFormat = SimpleDateFormat("dd-MM-yyyy")
    return outFormat.format(date)
}