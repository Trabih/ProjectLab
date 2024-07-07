// Daos.kt
package com.example.projectlab1

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProjectDao {
    @Query("SELECT * FROM projects")
    suspend fun getAllProjects(): List<Project>

    @Query("SELECT * FROM projects WHERE id = :projectId")
    suspend fun getProjectById(projectId: Int): Project

    @Insert
    suspend fun insertProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)
}

@Dao
interface PekerjaanDao {
    @Query("SELECT * FROM pekerjaans WHERE projectId = :projectId")
    suspend fun getPekerjaansByProjectId(projectId: Int): List<Pekerjaan>

    @Query("SELECT * FROM pekerjaans WHERE id = :pekerjaanId")
    suspend fun getPekerjaanById(pekerjaanId: Int): Pekerjaan

    @Insert
    suspend fun insertPekerjaan(pekerjaan: Pekerjaan)

    @Update
    suspend fun updatePekerjaan(pekerjaan: Pekerjaan)

    @Delete
    suspend fun deletePekerjaan(pekerjaan: Pekerjaan)
}

@Dao
interface AktivitasDao {
    @Query("SELECT * FROM aktivitas WHERE pekerjaanId = :pekerjaanId")
    suspend fun getAktivitasByPekerjaanId(pekerjaanId: Int): List<Aktivitas>

    @Query("SELECT * FROM aktivitas WHERE id = :aktivitasId")
    suspend fun getAktivitasById(aktivitasId: Int): Aktivitas

    @Insert
    suspend fun insertAktivitas(aktivitas: Aktivitas)

    @Update
    suspend fun updateAktivitas(aktivitas: Aktivitas)

    @Delete
    suspend fun deleteAktivitas(aktivitas: Aktivitas)
}
