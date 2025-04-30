package com.example.mapsapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Student
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            database.insertStudent(newStudent)
            getAllStudents()
        }
    }

    fun updateStudent(id: String, name: String, mark: String){
        CoroutineScope(Dispatchers.IO).launch {
            database.updateStudent(id, name, mark.toDouble())
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
}