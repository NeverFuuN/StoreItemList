package com.example.testworknode.presentation.Login

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.example.testworknode.data.NotesFBImp
import com.example.testworknode.presentation.APP_ACTIVITY
import com.example.testworknode.presentation.REPOSITORY

class LoginViewModel (application: Application) : AndroidViewModel(application) {

    init {
        REPOSITORY = NotesFBImp()
    }
//    private val loginUser= LoginUserUseCase(REPOSITORY)

    fun initLogin(onSuccess: () -> Unit) {
//        loginUser.loginUser(onSuccess) {
//            Toast.makeText(APP_ACTIVITY, it, Toast.LENGTH_SHORT).show()
//        REPOSITORY = NotesFBImp()
        REPOSITORY.loginUser(onSuccess) {
            Toast.makeText(APP_ACTIVITY, it, Toast.LENGTH_SHORT).show()
        }
    }
}
//    LoginUserUseCase(repository).apply {
//            loginUser({ onSuccess() },{Toast.makeText(APP_ACTIVITY,it,Toast.LENGTH_SHORT).show()})
//        }
//    }

