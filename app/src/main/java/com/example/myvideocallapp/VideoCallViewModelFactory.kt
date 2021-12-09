package com.example.myvideocallapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class VideoCallViewModelFactory(val name : String?, val authToken : String?, val application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if( modelClass.isAssignableFrom(VideoCallVm::class.java) ) {
            return VideoCallVm(name, authToken, application) as T
        } else {
            throw IllegalArgumentException("Unknown class ${modelClass.canonicalName}. Expected VideoCallVm to be requested.")
        }
    }
}