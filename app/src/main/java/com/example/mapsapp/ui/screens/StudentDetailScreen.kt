package com.example.mapsapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.MyViewModel
import androidx.compose.runtime.getValue


@Composable
fun StudentDetailScreen(studentId: String, navigateBack: () -> Unit) {
    val myViewModel = viewModel<MyViewModel>()
    myViewModel.getStudent(studentId)
    val studentName: String by myViewModel.studentName.observeAsState("")
    val studentMark: String by myViewModel.studentMark.observeAsState("")

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(value = studentName, onValueChange = { myViewModel.editStudentName(it) })
        TextField(value = studentMark, onValueChange = { myViewModel.editStudentMark(it) })
        Button(onClick = {
            myViewModel.updateStudent(studentId, studentName, studentMark, null)
            navigateBack()
        }) {
            Text("Update")
        }
    }
}


