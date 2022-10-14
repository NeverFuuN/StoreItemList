package com.example.testworknode.domain.useCase_not_used

import com.example.testworknode.domain.NotesRepository

class RegisterUserUseCase(private val repository: NotesRepository) {
    fun registerUser(onSuccess: () -> Unit,onFail: (String) -> Unit){
        return repository.registerUser({onSuccess()},{onFail(it.toString())})
    }
}