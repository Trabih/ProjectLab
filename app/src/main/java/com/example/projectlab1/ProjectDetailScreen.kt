package com.example.projectlab1

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch

class ProjectDetailScreen : AppCompatActivity() {
    private lateinit var viewModel: ProjectViewModel
    private var projectId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        projectId = intent.getIntExtra("projectId", -1)
        val appDatabase = (application as ProjectLab1Application).database
        val viewModelFactory = ProjectViewModelFactory(appDatabase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProjectViewModel::class.java)
        setContent {
            ProjectDetailScreenContent(viewModel, projectId)
        }
    }
}

@Composable
fun ProjectDetailScreenContent(viewModel: ProjectViewModel, projectId: Int) {
    val context = LocalContext.current
    var project by remember { mutableStateOf<Project?>(null) }
    val pekerjaanList by viewModel.pekerjaans.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(projectId) {
        coroutineScope.launch {
            project = viewModel.getProjectById(projectId)
            viewModel.loadPekerjaans(projectId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        project?.let {
            Text(text = it.namaProject, fontSize = 24.sp, color = Color.Black)
            Text(text = "Tujuan: ${it.tujuanProject}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Tanggal Mulai: ${it.tangmulProject}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Tanggal Selesai: ${it.tangselProject}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "PIC: ${it.picProject}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Status: ${it.statusProject}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Biaya: ${it.biaya}", fontSize = 18.sp, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                context.startActivity(
                    Intent(context, PekerjaanForm::class.java).putExtra("projectId", it.id)
                )
            }) {
                Text("Tambah Pekerjaan")
            }
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(pekerjaanList) { pekerjaan ->
                    PekerjaanCard(pekerjaan, viewModel)
                }
            }
        }
    }
}

@Composable
fun PekerjaanCard(pekerjaan: Pekerjaan, viewModel: ProjectViewModel) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = pekerjaan.namaPekerjaan, fontSize = 20.sp, color = Color.Black)
            Text(text = "Deskripsi: ${pekerjaan.deskripsiPekerjaan}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Tanggal Mulai: ${pekerjaan.tangmulPekerjaan}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Tempat Mulai: ${pekerjaan.tempatmulPekerjaan}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Tanggal Selesai: ${pekerjaan.tangselPekerjaan ?: "N/A"}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Tempat Selesai: ${pekerjaan.tempatselPekerjaan ?: "N/A"}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Pelaksana: ${pekerjaan.pelaksanaPekerjaan}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Supervisor: ${pekerjaan.supervisorPekerjaan}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Status: ${pekerjaan.statusPekerjaan}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Biaya: ${pekerjaan.biayaPekerjaan}", fontSize = 16.sp, color = Color.DarkGray)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(context, PekerjaanDetailScreen::class.java).putExtra("pekerjaanId", pekerjaan.id)
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "View")
                }
                Button(
                    onClick = {
                        viewModel.deletePekerjaan(pekerjaan)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}

@Composable
private fun <T> LiveData<T>.observeAsState(initial: T): State<T> {
    val state = remember { mutableStateOf(initial) }
    val observer = Observer<T> { state.value = it ?: initial }
    DisposableEffect(this) {
        observeForever(observer)
        onDispose { removeObserver(observer) }
    }
    return state
}
