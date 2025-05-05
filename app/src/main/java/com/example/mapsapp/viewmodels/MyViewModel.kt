package com.example.supabasetest.viewmodel

import android.graphics.Bitmap
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class MyViewModel: ViewModel() {

    val database = MyApp.database

    private val _studentsList = MutableLiveData<List<Student>>()
    val studentsList = _studentsList

    private var _selectedStudent: Student? = null

    private val _studentName = MutableLiveData<String>()
    val studentName = _studentName

    private val _studentMark = MutableLiveData<String>()
    val studentMark = _studentMark

    fun getAllStudents() {
        CoroutineScope(Dispatchers.IO).launch {
            val databaseStudents = database.getAllStudents()
            withContext(Dispatchers.Main) {
                _studentsList.value = databaseStudents
            }
        }
    }

    fun insertNewStudent(name: String, mark: String) {
        val newStudent = Student(name = name, mark = mark.toDouble())
        CoroutineScope(Dispatchers.IO).launch {
            database.insertStudent(name, mark.toDouble(), newStudent.toString())
            getAllStudents()
        }
    }

    fun deleteStudent(id: String){
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteStudent(id)
            getAllStudents()
        }
    }

    fun getStudent(id: String){
        if(_selectedStudent == null){
            CoroutineScope(Dispatchers.IO).launch {
                val student = database.getStudent(id)
                withContext(Dispatchers.Main) {
                    _selectedStudent = student
                    _studentName.value = student.name
                    _studentMark.value = student.mark.toString()
                }
            }
        }
    }

    fun editStudentName(name: String) {
        _studentName.value = name
    }

    fun editStudentMark(mark: String) {
        _studentMark.value = mark
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNewStudent(name: String, mark: String, image: Bitmap?) {
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        CoroutineScope(Dispatchers.IO).launch {
            val imageName = database.uploadImage(stream.toByteArray())
            database.insertStudent(name, mark.toDouble(), imageName)
        }
    }

    fun updateStudent(id: String, name: String, mark: String, image: Bitmap?){
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        val imageName = _selectedStudent.value?.image?.removePrefix("https://aobflzinjcljzqpxpcxs.supabase.co/storage/v1/object/public/images/")
        CoroutineScope(Dispatchers.IO).launch {
            database.updateStudent(id, name, mark.toDouble(), imageName.toString(), stream.toByteArray())
        }
    }

}