package com.example.mapsapp.ui.screens
/*
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mapsapp.viewmodels.ViewModelMap.MyViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mapsapp.data.Marker


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StudentsScreen(navigateToDetail: (String) -> Unit) {

    val myViewModel = viewModel<MyViewModel>()

    val markerList by myViewModel.markersList.observeAsState(emptyList<Marker>())

    val markerName: String by myViewModel.studentName.observeAsState("")
    val markerMark: String by myViewModel.studentMark.observeAsState("")

    myViewModel.getAllMarkers()

    Column(
            Modifier.fillMaxSize()
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Create new student", fontSize = 28.sp, fontWeight = FontWeight.Bold)

            TextField(value = markerName, onValueChange = { myViewModel.editStudentName(it) })
            TextField(value = markerMark, onValueChange = { myViewModel.editStudentMark(it) })


            Button(onClick = {
                myViewModel.insertNewStudent(markerName, markerMark,)
            }) {
                Text("Insert")
            }
        }
        Text(
            "Students List",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .weight(0.6f)
        ) {
            items(markerList) { marker ->
                val dissmissState = rememberSwipeToDismissBoxState(
                    confirmValueChange = {
                        if (it == SwipeToDismissBoxValue.EndToStart) {
                            myViewModel.deleteStudent(marker.id)
                            true
                        } else {
                            false
                        }
                    }
                )
                SwipeToDismissBox(state = dissmissState, backgroundContent = {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .background(Color.Red)
                            .padding(horizontal = 20.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                    }
                }) {
                    StudentItem(marker) { navigateToDetail(marker.id) }
                }
            }
        }
    }
}

@Composable
fun StudentItem(student: Marker, navigateToDetail: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.LightGray)
            .border(width = 2.dp, Color.DarkGray)
            .clickable { navigateToDetail(student.id.toString()) }) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(student.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "Mark: ${student.mark}")
        }
    }
}

 */


