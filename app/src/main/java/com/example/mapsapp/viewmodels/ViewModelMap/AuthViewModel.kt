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




class AuthViewModel(private val sharedPreferences: SharedPreferencesHelper) : ViewModel() {

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


    //Varibles para mapa
    private val _markersList = MutableLiveData<List<Marker>>()
    val markersList = _markersList

    private var _selectedMarker: Marker? = null

    private val _loading = MutableLiveData(true)
    val loading = _loading

    private val _markerName = MutableLiveData<String>()
    val markerName = _markerName

    private val _markerMark = MutableLiveData<String>()
    val markerMark = _markerMark

    val _markerImageUrl = MutableLiveData<String?>()
    val markerImageUrl = _markerImageUrl


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
                val marker = database.getMarker(id)
                withContext(Dispatchers.Main) {
                    _selectedMarker = marker
                    _markerName.value = marker?.name
                    _markerMark.value = marker?.mark
                    _markerImageUrl.value = marker?.imageUrl
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun insertNewMarker(
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
            database.insertMarker(
                Marker(
                    name = name,
                    mark = mark,
                    imageUrl = imageName,
                    latitude = latitude,
                    longitude = longitude,
                )
            )
        }
    }

    fun deleteMark(id: Int, image: String) {
        CoroutineScope(Dispatchers.IO).launch {
            database.deleteImage(image)
            database.deleteMarker(id)
            getAllMarkers()
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun updateMarker(name: String, mark: String, image: Bitmap?, onComplete: (() -> Unit)? = null) {
        val stream = ByteArrayOutputStream()
        image?.compress(Bitmap.CompressFormat.PNG, 0, stream)
        Log.d("Marc", "es null? ${image == null}")

        CoroutineScope(Dispatchers.IO).launch {
            // Eliminar el marcador existente
            _selectedMarker?.let { marker ->
                database.deleteMarker(marker.id)
                marker.imageUrl?.let { database.deleteImage(it) }
            }

            // Subir la nueva imagen
            val newImageUrl = database.uploadImage(stream.toByteArray())

            // Agregar el marcador actualizado
            database.insertMarker(
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


    fun editStudentMark(mark: String) {
        _markerMark.value = mark
    }

    fun editStudentName(name: String) {
        _markerName.value = name
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

    fun errorMessageShowed() {
        _showError.value = false
    }

    fun logout() {
        viewModelScope.launch {
            sharedPreferences.clear()
            _authState.value = AuthState.Unauthenticated
        }
    }
}