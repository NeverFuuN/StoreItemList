package com.example.testworknode.presentation.Registration

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.testworknode.data.NotesFBImp
import com.example.testworknode.presentation.APP_ACTIVITY
import com.example.testworknode.presentation.REPOSITORY

class RegisterViewModel(application: Application) :AndroidViewModel(application) {
    fun registration(onSuccess: () -> Unit){
        REPOSITORY.registerUser({onSuccess()},{Toast.makeText(APP_ACTIVITY,it,Toast.LENGTH_SHORT).show()})
    }
}