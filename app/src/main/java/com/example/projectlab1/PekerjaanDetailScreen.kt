package com.example.projectlab1

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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

class PekerjaanDetailScreen : AppCompatActivity() {
    private lateinit var viewModel: ProjectViewModel
    private var pekerjaanId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pekerjaanId = intent.getIntExtra("pekerjaanId", -1)
        val appDatabase = (application as ProjectLab1Application).database
        val viewModelFactory = ProjectViewModelFactory(appDatabase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProjectViewModel::class.java)
        setContent {
            PekerjaanDetailScreenContent(viewModel, pekerjaanId)
        }
    }
}

@Composable
fun PekerjaanDetailScreenContent(viewModel: ProjectViewModel, pekerjaanId: Int) {
    val context = LocalContext.current
    var pekerjaan by remember { mutableStateOf<Pekerjaan?>(null) }
    val aktivitasList by viewModel.aktivitasList.observeAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(pekerjaanId) {
        coroutineScope.launch {
            pekerjaan = viewModel.getPekerjaanById(pekerjaanId)
            viewModel.loadAktivitas(pekerjaanId)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        pekerjaan?.let {
            Text(text = it.namaPekerjaan, fontSize = 24.sp, color = Color.Black)
            Text(text = "Deskripsi: ${it.deskripsiPekerjaan}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Tanggal Mulai: ${it.tangmulPekerjaan}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Tempat Mulai: ${it.tempatmulPekerjaan}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Tanggal Selesai: ${it.tangselPekerjaan}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Tempat Selesai: ${it.tempatselPekerjaan ?: "N/A"}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Pelaksana: ${it.pelaksanaPekerjaan}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Supervisor: ${it.supervisorPekerjaan}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Status: ${it.statusPekerjaan}", fontSize = 18.sp, color = Color.DarkGray)
            Text(text = "Biaya: ${it.biayaPekerjaan}", fontSize = 18.sp, color = Color.DarkGray)

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                context.startActivity(
                    Intent(context, AktivitasForm::class.java).putExtra("pekerjaanId", it.id)
                )
            }) {
                Text("Tambah Aktivitas")
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Aktivitas:", fontSize = 20.sp, color = Color.Black)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(aktivitasList) { aktivitas ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            // Handle click if needed
                        },
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = aktivitas.namaAktivitas, fontSize = 18.sp, color = Color.Black)
                            Text(text = "Status: ${aktivitas.statusAktivitas}", fontSize = 14.sp, color = Color.DarkGray)
                        }
                        IconButton(onClick = {
                            viewModel.deleteAktivitas(aktivitas)
                        }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
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
