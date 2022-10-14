package com.example.testworknode.domain.useCase_not_used

import com.example.testworknode.domain.Note
import com.example.testworknode.domain.NotesRepository

class DeleteNoteUseCase (private val repository: NotesRepository) {
     fun delete(note: Note, onSuccess: ()-> Unit){
        return repository.delete(note,{onSuccess})
    }
}