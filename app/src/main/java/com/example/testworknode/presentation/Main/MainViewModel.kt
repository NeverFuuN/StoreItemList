package com.example.testworknode.presentation.Main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.testworknode.data.NotesFBImp
import com.example.testworknode.domain.Note
import com.example.testworknode.presentation.AUTH
import com.example.testworknode.presentation.REPOSITORY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    val allPost = REPOSITORY.getNotesList()

    fun singOut(){
        AUTH.signOut()
    }

    fun delPost(note: Note, onSuccess: () -> Unit) = viewModelScope.launch ( Dispatchers.Main ){
        REPOSITORY.delete(note){
            onSuccess()
        }
    }
}