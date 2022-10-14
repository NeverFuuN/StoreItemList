package com.example.testworknode.domain.useCase_not_used

import com.example.testworknode.domain.NotesRepository

class GetNotesUseCase(private val repository: NotesRepository) {
    operator fun invoke() = repository.getNotesList()
}