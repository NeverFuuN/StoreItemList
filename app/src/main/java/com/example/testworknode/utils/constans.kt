package com.example.testworknode.presentation

import com.example.testworknode.domain.NotesRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var CURRENT_ID: String
lateinit var REF_DATABASE: DatabaseReference
lateinit var STOR_DATABASE: StorageReference
lateinit var APP_ACTIVITY: MainActivity
lateinit var REPOSITORY: NotesRepository
lateinit var EMAIL: String
lateinit var PASSWORD: String
const val ID_FIREBASE = "idFirebase"
const val NAME = "name"
const val COUNT = "count"
const val PRICE = "price"
const val IMG_URL = "imgUrl"

var save_Name:String =""
var save_Count:String =""
var save_Price:String = ""
lateinit var NAME_ITEM: String