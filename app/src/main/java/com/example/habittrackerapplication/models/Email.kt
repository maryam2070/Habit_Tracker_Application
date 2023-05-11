package com.example.habittrackerapplication.models

import com.google.firebase.database.Exclude


data class Email(val email:String)
{
    @Exclude
    fun toMap(value:String): Map<String, Any?> {
        return mapOf(
            value to email
        )
    }
}