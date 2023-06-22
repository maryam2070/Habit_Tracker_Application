package com.example.habittrackerapplication.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Calendar
import java.util.HashMap
import kotlin.collections.ArrayList


@Parcelize
data class Habit(
    var id: String?="",
    var name:String?="",
    var days:Int?=0,
    var completedDays: ArrayList<String> = arrayListOf(""),
    var iconPosition: Int=1,
    var isChecked:Boolean=false,
    var dayOfWeek:String?="Saturday",
    var listOfDays: ArrayList<String> = ArrayList<String>(),
    var dayOfMonth:Int?=1,
    var type:String="daily_habit",
    var timeOfAdding:Long=Calendar.getInstance().timeInMillis): Parcelable

data class HabitDTO(
    var id: String?="",
    val name:String?="",
    val days:Int?=0,
    val completedDays: ArrayList<String> = ArrayList<String>(),
    val iconPosition: Int=1,
    var isChecked:Boolean=false,
    val dayOfWeek:String?="Saturday",
    val listOfDays: ArrayList<String> = ArrayList<String>(),
    val dayOfMonth:Int?=1,
    val type:String="daily_habit")

fun habitDtoToHabit(taskObj: HashMap<*, *>): Habit {
    var habit=Habit()
    habit.id = taskObj ["id"].toString()
    var list=ArrayList<String>()
    if(taskObj ["completedDays"] !=null)
        list =(taskObj ["completedDays"] as java.util.ArrayList<String>)
    habit.completedDays = list
    habit.dayOfMonth = taskObj ["dayOfMonth"].toString().toInt()
    habit.days = taskObj ["days"].toString().toInt()
    habit.iconPosition = taskObj ["iconPosition"].toString().toInt()
    habit.dayOfWeek = taskObj ["dayOfWeek"].toString()
    habit.isChecked = taskObj ["isChecked"].toString().toBoolean()
    habit.name= taskObj ["name"].toString()
    habit.type= taskObj ["type"].toString()
    habit.listOfDays= taskObj ["listOfDays"] as java.util.ArrayList<String>
    habit.timeOfAdding=taskObj["timeOfAdding"].toString().toLong()
    return habit
}
