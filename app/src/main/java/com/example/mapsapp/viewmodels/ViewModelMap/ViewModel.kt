package com.example.mapsapp.viewmodels.ViewModelMap

import android.graphics.Bitmap
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapsapp.SupabaseApplication
import com.example.mapsapp.data.Marker
import com.example.mapsapp.utils.AuthState
import com.example.mapsapp.utils.SharedPreferencesHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ViewModel(private val sharedPreferences: SharedPreferencesHelper) : ViewModel() {

    //Variables para la base de datos
    val database = SupabaseApplication.auth


    //Variables para auth
    private val _email = MutableLiveData<String>()
    val email = _email

    private val _password = MutableLiveData<String>()
    val password = _password

    private val _authState = MutableLiveData<AuthState>()
    val authState = _authState

    private val _showError = MutableLiveData<Boolean>(false)
    val showError = _showError

    private val _user = MutableLiveData<String?>()
    val user = _user

    private val _refresh = MutableLiveData<Boolean>(false)
    val refresh = _refresh


    //Varibles para mapa
    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList

    private var _selectedMarker: Marker? = null

    private val _loading = MutableLiveData(true)
    val loading = _loading

    private val _studentName = MutableLiveData<String>()
    val studentName = _studentName

    private val _studentMark = MutableLiveData<String>()
    val studentMark = _studentMark

    val _studentImageUrl = MutableLiveData<String?>()
    val studentImageUrl = _studentImageUrl


    init {
        checkExistingSession()
    }


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
        if (_selectedMarker == null) {
            CoroutineScope(Dispatchers.IO).launch {
                val marker = database.getStudent(id)
                withContext(Dispatchers.Main) {
                    _selectedMarker = marker
                    _studentName.value = marker.name
                    _studentMark.value = marker.mark
                    _studentImageUrl.value = marker.imageUrl
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNewStudent(
        name: String,
        mark: String,
        image: Bitmap?,
        latitude: Double,
        longitude: Double
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val stream = ByteArrayOutputStream()
            image?.compress(Bitmap.CompressFormat.PNG, 0, stream)

            val imageName = database.uploadImage(stream.toByteArray())
            Log.d("MyViewModel", "Image name: $imageName")
            database.insertStudent(
                Marker(
                    name = name,
                    mark = mark,
                    imageUrl = imageName,
                    latitude = latitude,
                    longitude = longitude
                )
            )
        }
    }

    fun deleteMark(id: Int, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteImage(image)
            database.deleteStudent(id)
            getAllMarkers()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMarker(name: String, mark: String, image: Bitmap?) {
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        Log.d("Marc", "es null? ${image == null}")
        val baseUrl = "https://luxphgkqoavsmerxhoka.supabase.co/storage/v1/object/public/images/"

        CoroutineScope(Dispatchers.IO).launch {
            //Eliminar el marcador existente
            _selectedMarker?.let { marker ->
                database.deleteStudent(marker.id)
                marker.imageUrl?.let { database.deleteImage(it) }
            }

            //Subir la nueva imagen
            val newImageName = database.uploadImage(stream.toByteArray())
            val newImageUrl = "$baseUrl$newImageName"

            //Agregar el marcador actualizado
            database.insertStudent(
                Marker(
                    id = _selectedMarker?.id ?: 0,
                    name = name,
                    mark = mark,
                    imageUrl = newImageUrl,
                    latitude = _selectedMarker?.latitude ?: 0.0,
                    longitude = _selectedMarker?.longitude ?: 0.0
                )
            )
        }
    }

    fun editStudentName(name: String) {
        _studentName.value = name
    }

    fun editStudentMark(mark: String) {
        _studentMark.value = mark
    }


    //Verificacion
    fun editEmail(value: String) {
        _email.value = value
    }

    fun editPassword(value: String) {
        _password.value = value
    }


    private fun checkExistingSession() {
        viewModelScope.launch {
            val accessToken = sharedPreferences.getAccessToken()
            val refreshToken = sharedPreferences.getRefreshToken()
            when {
                !accessToken.isNullOrEmpty() -> refreshToken()
                !refreshToken.isNullOrEmpty() -> refreshToken()
                else -> _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            _authState.value = database.signUpWithEmail(_email.value!!, _password.value!!)
            if (_authState.value is AuthState.Error) {
                Log.d("MAVOI VIEW MODEL ERROR", _authState.value.toString())
                _showError.value = true
            } else {
                Log.d("MAVOI VIEW MODEL", _authState.value.toString())
                val session = database.retrieveCurrentSession()
                sharedPreferences.saveAuthData(
                    session!!.accessToken,
                    session.refreshToken
                )
            }
            _refresh.value = true
        }
    }

    fun signIn() {
        viewModelScope.launch {
            _authState.value = database.signInWithEmail(_email.value!!, _password.value!!)
            if (_authState.value is AuthState.Error) {
                _showError.value = true
            } else {
                val session = database.retrieveCurrentSession()
                sharedPreferences.saveAuthData(
                    session!!.accessToken,
                    session.refreshToken
                )
            }
        }
    }

    private fun refreshToken() {
        viewModelScope.launch {
            try {
                database.refreshSession()
                _authState.value = AuthState.Authenticated
            } catch (e: Exception) {
                sharedPreferences.clear()
                _authState.value = AuthState.Unauthenticated
            }
        }
    }

    fun errorMessageShowed(){
        _showError.value = false
    }

    fun logout() {
        viewModelScope.launch {
            sharedPreferences.clear()
            _authState.value = AuthState.Unauthenticated
        }
    }
}