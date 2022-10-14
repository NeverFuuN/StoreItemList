package com.example.testworknode.domain.useCase_not_used

import com.example.testworknode.domain.NotesRepository

class ForeignPassUserUseCase (private val repository: NotesRepository){
    fun foreignPass(onSuccess: () -> Unit,onFail: (String) -> Unit){
        return repository.foreignPassUser({onSuccess()},{onFail(it)})
    }
}