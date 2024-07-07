package com.example.projectlab1

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

class PekerjaanForm : AppCompatActivity() {
    private lateinit var viewModel: ProjectViewModel
    private var projectId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectId = intent.getIntExtra("projectId", -1)
        val appDatabase = (application as ProjectLab1Application).database
        val viewModelFactory = ProjectViewModelFactory(appDatabase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProjectViewModel::class.java)
        setContent {
            PekerjaanFormScreen(viewModel, projectId) {
                // Navigate back to ProjectDetailScreen
                val intent = Intent(this, ProjectDetailScreen::class.java).apply {
                    putExtra("projectId", projectId)
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
                finish()
            }
        }
    }
}

@Composable
fun PekerjaanFormScreen(viewModel: ProjectViewModel, projectId: Int, onSave: () -> Unit) {
    var namaPekerjaan by remember { mutableStateOf(TextFieldValue("")) }
    var deskripsiPekerjaan by remember { mutableStateOf(TextFieldValue("")) }
    var tangmulPekerjaan by remember { mutableStateOf(TextFieldValue("")) }
    var tempatmulPekerjaan by remember { mutableStateOf(TextFieldValue("")) }
    var tangselPekerjaan by remember { mutableStateOf(TextFieldValue("")) }
    var tempatselPekerjaan by remember { mutableStateOf(TextFieldValue("")) }
    var pelaksanaPekerjaan by remember { mutableStateOf(TextFieldValue("")) }
    var supervisorPekerjaan by remember { mutableStateOf(TextFieldValue("")) }
    var statusPekerjaan by remember { mutableStateOf(TextFieldValue("")) }
    var biayaPekerjaan by remember { mutableStateOf(TextFieldValue("")) }

    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Add Pekerjaan", fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = namaPekerjaan,
            onValueChange = { namaPekerjaan = it },
            label = { Text("Nama Pekerjaan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = deskripsiPekerjaan,
            onValueChange = { deskripsiPekerjaan = it },
            label = { Text("Deskripsi Pekerjaan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tangmulPekerjaan,
            onValueChange = { tangmulPekerjaan = it },
            label = { Text("Tanggal Mulai Pekerjaan (dd-MM-yyyy)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tempatmulPekerjaan,
            onValueChange = { tempatmulPekerjaan = it },
            label = { Text("Tempat Mulai Pekerjaan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tangselPekerjaan,
            onValueChange = { tangselPekerjaan = it },
            label = { Text("Tanggal Selesai Pekerjaan (dd-MM-yyyy)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = tempatselPekerjaan,
            onValueChange = { tempatselPekerjaan = it },
            label = { Text("Tempat Selesai Pekerjaan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = pelaksanaPekerjaan,
            onValueChange = { pelaksanaPekerjaan = it },
            label = { Text("Pelaksana Pekerjaan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = supervisorPekerjaan,
            onValueChange = { supervisorPekerjaan = it },
            label = { Text("Supervisor Pekerjaan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = statusPekerjaan,
            onValueChange = { statusPekerjaan = it },
            label = { Text("Status Pekerjaan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = biayaPekerjaan,
            onValueChange = { biayaPekerjaan = it },
            label = { Text("Biaya Pekerjaan") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val pekerjaan = Pekerjaan(
                    projectId = projectId,
                    namaPekerjaan = namaPekerjaan.text,
                    deskripsiPekerjaan = deskripsiPekerjaan.text,
                    tangmulPekerjaan = formatter.parse(tangmulPekerjaan.text) ?: Date(),
                    tempatmulPekerjaan = tempatmulPekerjaan.text,
                    tangselPekerjaan = formatter.parse(tangselPekerjaan.text),
                    tempatselPekerjaan = tempatselPekerjaan.text,
                    pelaksanaPekerjaan = pelaksanaPekerjaan.text,
                    supervisorPekerjaan = supervisorPekerjaan.text,
                    statusPekerjaan = statusPekerjaan.text,
                    biayaPekerjaan = biayaPekerjaan.text.toDoubleOrNull() ?: 0.0
                )
                viewModel.insertPekerjaan(pekerjaan)
                onSave()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Pekerjaan")
        }
    }
}
