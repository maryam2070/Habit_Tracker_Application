package com.example.habittrackerapplication.common

import com.google.firebase.auth.FirebaseUser

interface DataListener {
    fun onCompleted(data: Any?)
    fun onError(error: Throwable?)
}

sealed class Resource<T>(var data: T? = null, val message: String? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}