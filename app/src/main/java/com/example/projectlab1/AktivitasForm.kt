package com.example.projectlab1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AktivitasForm : AppCompatActivity() {
    private lateinit var viewModel: ProjectViewModel
    private var pekerjaanId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pekerjaanId = intent.getIntExtra("pekerjaanId", -1)
        val appDatabase = (application as ProjectLab1Application).database
        val viewModelFactory = ProjectViewModelFactory(appDatabase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ProjectViewModel::class.java)
        setContent {
            AktivitasFormScreen(viewModel, pekerjaanId) {
                // Navigate back to PekerjaanDetailScreen
                val intent = Intent(this, PekerjaanDetailScreen::class.java)
                intent.putExtra("pekerjaanId", pekerjaanId)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }
        }
    }
}

@Composable
fun AktivitasFormScreen(viewModel: ProjectViewModel, pekerjaanId: Int, onSave: () -> Unit) {
    var namaAktivitas by remember { mutableStateOf(TextFieldValue("")) }
    var waktuPelaksanaan by remember { mutableStateOf(TextFieldValue("")) }
    var pelaksanaAktivitas by remember { mutableStateOf(TextFieldValue("")) }
    var evaluasiAktivitas by remember { mutableStateOf(TextFieldValue("")) }
    var rencanaAktivitas by remember { mutableStateOf(TextFieldValue("")) }
    var statusAktivitas by remember { mutableStateOf(TextFieldValue("")) }
    var biayaAktivitas by remember { mutableStateOf(TextFieldValue("")) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var imageUrl by remember { mutableStateOf<String?>(null) }

    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let {
                val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                selectedImageBitmap = bitmap
                imageUrl = it.toString()
            }
        }
    )

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = { bitmap ->
            selectedImageBitmap = bitmap
            // No need to save to file, just handle the bitmap directly
        }
    )

    val requestCameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                cameraLauncher.launch(null)
            } else {
                // Handle the case where the user denied the permission
                // You can show a message to the user here
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Add Aktivitas", fontSize = 24.sp, color = Color.Black)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = namaAktivitas,
            onValueChange = { namaAktivitas = it },
            label = { Text("Nama Aktivitas") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = waktuPelaksanaan,
            onValueChange = { waktuPelaksanaan = it },
            label = { Text("Waktu Pelaksanaan (dd-MM-yyyy)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = pelaksanaAktivitas,
            onValueChange = { pelaksanaAktivitas = it },
            label = { Text("Pelaksana Aktivitas") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = evaluasiAktivitas,
            onValueChange = { evaluasiAktivitas = it },
            label = { Text("Evaluasi Aktivitas") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = rencanaAktivitas,
            onValueChange = { rencanaAktivitas = it },
            label = { Text("Rencana Aktivitas") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = statusAktivitas,
            onValueChange = { statusAktivitas = it },
            label = { Text("Status Aktivitas") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = biayaAktivitas,
            onValueChange = { biayaAktivitas = it },
            label = { Text("Biaya Aktivitas") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                Text("Select Photo")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    cameraLauncher.launch(null)
                } else {
                    requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
                Text("Take Photo")
            }
        }

        selectedImageBitmap?.let { bitmap ->
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(128.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val aktivitas = Aktivitas(
                    pekerjaanId = pekerjaanId,
                    namaAktivitas = namaAktivitas.text,
                    waktuPelaksanaan = formatter.parse(waktuPelaksanaan.text) ?: Date(),
                    pelaksanaAktivitas = pelaksanaAktivitas.text,
                    evaluasiAktivitas = evaluasiAktivitas.text,
                    rencanaAktivitas = rencanaAktivitas.text,
                    statusAktivitas = statusAktivitas.text,
                    biayaAktivitas = biayaAktivitas.text.toDoubleOrNull() ?: 0.0,
                    imageUrl = imageUrl  // Add this line for the image URL
                )
                viewModel.insertAktivitas(aktivitas)
                onSave()  // Call onSave here
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Aktivitas")
        }
    }
}
