package com.example.projectlab1

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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.launch

class AktivitasDetailScreen : AppCompatActivity() {
    private lateinit var viewModel: ProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val appDatabase = (application as ProjectLab1Application).database
        val viewModelFactory = ProjectViewModelFactory(appDatabase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProjectViewModel::class.java)
        setContent {
            AktivitasDetailScreenContent(viewModel)
        }
    }
}

@Composable
fun AktivitasDetailScreenContent(viewModel: ProjectViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var aktivitasList by remember { mutableStateOf(emptyList<Aktivitas>()) }
    var selectedAktivitas by remember { mutableStateOf<Aktivitas?>(null) }

    LaunchedEffect(Unit) {
        viewModel.loadAktivitas(1) // Replace 1 with the actual pekerjaanId
    }

    val aktivitasListState by viewModel.aktivitasList.observeAsState(emptyList())

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .background(Color.White)
        ) {
            items(aktivitasListState) { aktivitas ->
                AktivitasCard(aktivitas = aktivitas, viewModel = viewModel, onClickView = { selectedAktivitas = it })
            }
        }

        selectedAktivitas?.let { aktivitas ->
            AktivitasDetailView(aktivitas)
        }
    }
}

@Composable
fun AktivitasCard(aktivitas: Aktivitas, viewModel: ProjectViewModel, onClickView: (Aktivitas) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = aktivitas.namaAktivitas, fontSize = 20.sp, color = Color.Black)
            Text(text = "Pelaksana: ${aktivitas.pelaksanaAktivitas}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Status: ${aktivitas.statusAktivitas}", fontSize = 16.sp, color = Color.DarkGray)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Button(
                    onClick = { onClickView(aktivitas) },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "View")
                }
                Button(
                    onClick = {
                        viewModel.deleteAktivitas(aktivitas)
                        viewModel.loadAktivitas(aktivitas.pekerjaanId)
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
fun AktivitasDetailView(aktivitas: Aktivitas) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(text = aktivitas.namaAktivitas, fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Pelaksana: ${aktivitas.pelaksanaAktivitas}", fontSize = 16.sp, color = Color.DarkGray)
        Text(text = "Status: ${aktivitas.statusAktivitas}", fontSize = 16.sp, color = Color.DarkGray)
        Text(text = "Evaluasi: ${aktivitas.evaluasiAktivitas ?: "N/A"}", fontSize = 16.sp, color = Color.DarkGray)
        Text(text = "Rencana: ${aktivitas.rencanaAktivitas ?: "N/A"}", fontSize = 16.sp, color = Color.DarkGray)
        Text(text = "Biaya: ${aktivitas.biayaAktivitas}", fontSize = 16.sp, color = Color.DarkGray)
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
