package com.example.projectlab1

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProjectForm : AppCompatActivity() {
    private lateinit var viewModel: ProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appDatabase = (application as ProjectLab1Application).database
        val viewModelFactory = ProjectViewModelFactory(appDatabase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProjectViewModel::class.java)
        setContent {
            ProjectFormScreen(viewModel) {
                // Navigate back to MainActivity
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}

@Composable
fun ProjectFormScreen(viewModel: ProjectViewModel, onSave: () -> Unit) {
    var namaProject by remember { mutableStateOf(TextFieldValue("")) }
    var tujuanProject by remember { mutableStateOf(TextFieldValue("")) }
    var tangmulProject by remember { mutableStateOf(TextFieldValue("")) }
    var tangselProject by remember { mutableStateOf(TextFieldValue("")) }
    var picProject by remember { mutableStateOf(TextFieldValue("")) }
    var statusProject by remember { mutableStateOf(TextFieldValue("")) }
    var biaya by remember { mutableStateOf(TextFieldValue("")) }

    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(text = "Add Project", fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = namaProject,
            onValueChange = { namaProject = it },
            label = { Text("Nama Project") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tujuanProject,
            onValueChange = { tujuanProject = it },
            label = { Text("Tujuan Project") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tangmulProject,
            onValueChange = { tangmulProject = it },
            label = { Text("Tanggal Mulai Project") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tangselProject,
            onValueChange = { tangselProject = it },
            label = { Text("Tanggal Selesai Project") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = picProject,
            onValueChange = { picProject = it },
            label = { Text("PIC Project") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = statusProject,
            onValueChange = { statusProject = it },
            label = { Text("Status Project") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = biaya,
            onValueChange = { biaya = it },
            label = { Text("Biaya") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val project = Project(
                    namaProject = namaProject.text,
                    tujuanProject = tujuanProject.text,
                    tangmulProject = formatter.parse(tangmulProject.text) ?: Date(),
                    tangselProject = formatter.parse(tangselProject.text) ?: Date(),
                    picProject = apicProject.text,
                    statusProject = statusProject.text,
                    biaya = biaya.text.toDoubleOrNull() ?: 0.0
                )
                viewModel.insertProject(project)
                onSave()  // Call onSave here
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Project")
        }
    }
}
