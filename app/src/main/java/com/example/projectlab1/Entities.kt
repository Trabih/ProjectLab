// Entities.kt
package com.example.projectlab1

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "projects")
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val namaProject: String,
    val tujuanProject: String,
    val tangmulProject: Date,
    val tangselProject: Date,
    val picProject: String,
    val statusProject: String,
    val biaya: Double = 0.0
)
@Entity(tableName = "pekerjaans")
data class Pekerjaan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val projectId: Int,
    val namaPekerjaan: String,
    val deskripsiPekerjaan: String,
    val tangmulPekerjaan: Date,
    val tempatmulPekerjaan: String,
    val tangselPekerjaan: Date?,
    val tempatselPekerjaan: String?,
    val pelaksanaPekerjaan: String,
    val supervisorPekerjaan: String,
    val statusPekerjaan: String,
    val biayaPekerjaan: Double = 0.0
)

@Entity(tableName = "aktivitas")
data class Aktivitas(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pekerjaanId: Int,
    val namaAktivitas: String,
    val waktuPelaksanaan: Date,
    val pelaksanaAktivitas: String,
    val evaluasiAktivitas: String?,
    val rencanaAktivitas: String?,
    val statusAktivitas: String,
    val biayaAktivitas: Double = 0.0,
    val imageUrl: String? = null
)
