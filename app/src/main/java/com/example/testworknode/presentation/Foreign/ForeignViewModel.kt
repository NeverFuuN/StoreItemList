package com.example.testworknode.presentation.Foreign

import android.app.Application
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.testworknode.data.NotesFBImp
import com.example.testworknode.presentation.APP_ACTIVITY
import com.example.testworknode.presentation.AUTH
import com.example.testworknode.presentation.REPOSITORY

class ForeignViewModel(application: Application) : AndroidViewModel(application) {

    fun foreing(onSuccess: () -> Unit){
        REPOSITORY.foreignPassUser({ onSuccess() },{ Toast.makeText(APP_ACTIVITY,it, Toast.LENGTH_SHORT).show()})
    }
}