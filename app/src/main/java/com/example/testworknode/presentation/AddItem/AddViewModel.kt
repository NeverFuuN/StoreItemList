package com.example.testworknode.presentation.AddItem

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.testworknode.domain.Note
import com.example.testworknode.presentation.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddViewModel(application: Application): AndroidViewModel(application) {

    fun addPost(note: Note, onSuccess: ()-> Unit) = viewModelScope.launch(Dispatchers.Main) {
        REPOSITORY.addNote(note){
            onSuccess()
        }
    }
    fun delPost(note: Note, onSuccess: () -> Unit) = viewModelScope.launch ( Dispatchers.Main ){
        REPOSITORY.delete(note){
            onSuccess()
        }
    }
}