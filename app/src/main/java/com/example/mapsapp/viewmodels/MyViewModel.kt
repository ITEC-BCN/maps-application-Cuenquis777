package com.example.mapsapp.viewmodels

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mapsapp.MyApp
import com.example.mapsapp.data.Marker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class MyViewModel: ViewModel() {

    val database = MyApp.database


    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList

    private var _selectedStudent: Marker? = null

    private val _loading = MutableLiveData(true)
    val loading = _loading

    private val _studentName = MutableLiveData<String>()
    val studentName = _studentName

    private val _studentMark = MutableLiveData<String>()
    val studentMark = _studentMark

    val _studentImageUrl = MutableLiveData<String?>()
    val studentImageUrl = _studentImageUrl


    fun getAllMarkers() {
        CoroutineScope(Dispatchers.IO).launch {
            val databaseStudents = database.getAllMarkers()
            withContext(Dispatchers.Main) {
                _markersList.value = databaseStudents
                _loading.value = false

            }
        }
    }

    fun getMarker(id: Int) {
        if (_selectedStudent == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val marker = database.getStudent(id)
                withContext(Dispatchers.Main) {
                    _selectedStudent = marker
                    _studentName.value = marker.name
                    _studentMark.value = marker.mark
                    _studentImageUrl.value = marker.imageUrl
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNewStudent(name: String, mark: String, image: Bitmap?) {
        CoroutineScope(Dispatchers.IO).launch {
            val stream = ByteArrayOutputStream()
            image?.compress(Bitmap.CompressFormat.PNG, 0, stream)

            val imageName = database.uploadImage(stream.toByteArray())
            Log.d("MyViewModel", "Image name: $imageName")
            database.insertStudent(Marker(name = name, mark = mark, imageUrl = imageName))
        }
    }

    fun deleteStudent(id: Int, image: String){
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteImage(image)
            database.deleteStudent(id)
            getAllMarkers()
        }
    }


    fun updateMarker(name: String, mark: String, image: Bitmap?){
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        Log.d("Marc", "es null? ${image == null}")
        val imageName = _selectedStudent?.imageUrl?.removePrefix("https://luxphgkqoavsmerxhoka.supabase.co/storage/v1/object/public/images/")
        CoroutineScope(Dispatchers.IO).launch {
            database.updateMarker(name, mark, imageName.toString(), stream.toByteArray())
        }
    }

    fun editStudentName(name: String) {
        _studentName.value = name
    }

    fun editStudentMark(mark: String) {
        _studentMark.value = mark
    }
}