package com.example.testworknode.domain.useCase_not_used

import com.example.testworknode.domain.NotesRepository

class LoginUserUseCase(private val repository: NotesRepository) {
    fun loginUser(onSuccess: () -> Unit,onFail: (String) -> Unit){
        return repository.loginUser({onSuccess()},{onFail(it.toString())})
    }
}