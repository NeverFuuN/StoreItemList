package com.example.testworknode.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.testworknode.domain.Note
import com.example.testworknode.domain.NotesRepository
import com.example.testworknode.presentation.*
import com.example.testworknode.utils.AppPreference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class NotesFBImp : NotesRepository {

    init {
        AUTH = FirebaseAuth.getInstance()
    }

    override fun loginUser(onSuccess: () -> Unit, onFail: (String) -> Unit) {
        if (AppPreference.getInitUser()) {
            initReft()
            Log.d("TAG", "Юзер ${AUTH.currentUser?.uid.toString()}")
            onSuccess()
//            Log.d("TAG", "Юзер ${AUTH.currentUser?.uid.toString()}")
        } else {
            AUTH.signInWithEmailAndPassword(EMAIL, PASSWORD)
                .addOnSuccessListener {
                    onSuccess()
                    initReft()
                    Log.d("TAG", "Вход произведен")
                    Log.d("TAG", "Юзер ${AUTH.currentUser?.uid.toString()}")
                }
                .addOnFailureListener {
                    onFail(it.message.toString())
                    Log.d("TAG", "Ошибка :${it.message.toString()}")
                }
        }
    }

    override fun registerUser(onSuccess: () -> Unit, onFail: (String) -> Unit) {
        AUTH.createUserWithEmailAndPassword(EMAIL, PASSWORD)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("TAG", "createUserWithEmail:success")
                    onSuccess()
                } else {
                    onFail("Ошибка регистрации")
                    Log.w("TAG", "createUserWithEmail:failure", task.exception)
                }
            }
    }

    override fun foreignPassUser(onSuccess: () -> Unit, onFail: (String) -> Unit) {
        AUTH.sendPasswordResetEmail(EMAIL)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onSuccess()
                    Log.d("TAG", "Email sent.")
                } else {
                    onFail("Произошла ошибка, повторите запрос")
                }
            }

    }

    override fun getNotesList(): LiveData<List<Note>> {

        var listofData = MutableLiveData<List<Note>>()
        val postListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                val post = snapshot.children.map {
                    it.getValue(Note::class.java) ?: Note()
                }
                listofData.value = post
                Log.d("TAG", "DATA ${listofData.toString()}")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }
        REF_DATABASE.addValueEventListener(postListener)

        Log.d("TAG", "DATA2 ${listofData.toString()}")
        return listofData
    }

    override fun addNote(note: Note, onSuccess: () -> Unit) {
        val idNote = REF_DATABASE.push().key.toString()
        val mapNote = hashMapOf<String, Any>()
        mapNote[ID_FIREBASE] = idNote
        mapNote[NAME] = note.name
        mapNote[COUNT] = note.count
        mapNote[PRICE] = note.price
        mapNote[IMG_URL] = note.imgUrl

        REF_DATABASE.child(idNote)
            .updateChildren(mapNote)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { Log.d("TAG", it.message.toString()) }
    }

    private fun initReft() {
        CURRENT_ID = AUTH.currentUser?.uid.toString()
        REF_DATABASE = FirebaseDatabase.getInstance().reference
            .child(CURRENT_ID)
        STOR_DATABASE = FirebaseStorage.getInstance().reference
    }

    override fun signOut() {
        AUTH.signOut()
    }

    override fun delete(note: Note, onSuccess: () -> Unit) {
        Log.d("TAG", "$note")
        REF_DATABASE.child(note.idFirebase).removeValue()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { Log.d("TAG",it.message.toString()) }
    }

//    private fun loadImg(){
//        STOR_DATABASE = FirebaseStorage.getInstance().reference
////            .child(CURRENT_ID)
//        val ref = storageRef.child("images/mountains.jpg")
//        uploadTask = ref.putFile(file)
//
//        val urlTask = uploadTask.continueWithTask { task ->
//            if (!task.isSuccessful) {
//                task.exception?.let {
//                    throw it
//                }
//            }
//            ref.downloadUrl
//        }.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val downloadUri = task.result
//            } else {
//                // Handle failures
//                // ...
//            }
//        }
//
//    }
}