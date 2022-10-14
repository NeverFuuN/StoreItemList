package com.example.testworknode.domain

import androidx.lifecycle.LiveData

interface NotesRepository {

    fun loginUser(onSuccess: () -> Unit,onFail: (String) -> Unit)

    fun registerUser(onSuccess: () -> Unit,onFail: (String) -> Unit)

    fun foreignPassUser(onSuccess: () -> Unit,onFail: (String) -> Unit)

    fun getNotesList():LiveData<List<Note>>

    fun addNote(note: Note,  onSuccess: () -> Unit)

    fun signOut()

    fun delete(note: Note, onSuccess: () -> Unit)

}