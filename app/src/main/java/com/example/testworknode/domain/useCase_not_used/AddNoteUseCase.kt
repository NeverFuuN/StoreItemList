package com.example.testworknode.domain.useCase_not_used

import com.example.testworknode.domain.Note
import com.example.testworknode.domain.NotesRepository

class AddNoteUseCase(private val repository: NotesRepository) {
     fun addNote(note: Note, onSuccess: ()-> Unit){
        return repository.addNote(note,{onSuccess})
    }
}