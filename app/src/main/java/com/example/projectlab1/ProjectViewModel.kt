package com.example.projectlab1

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class ProjectViewModel(private val appDatabase: AppDatabase) : ViewModel() {

    private val projectDao = appDatabase.projectDao()
    private val pekerjaanDao = appDatabase.pekerjaanDao()
    private val aktivitasDao = appDatabase.aktivitasDao()

    private val _projects = MutableLiveData<List<Project>>()
    val projects: LiveData<List<Project>> get() = _projects

    private val _pekerjaans = MutableLiveData<List<Pekerjaan>>()
    val pekerjaans: LiveData<List<Pekerjaan>> get() = _pekerjaans

    private val _aktivitasList = MutableLiveData<List<Aktivitas>>()
    val aktivitasList: LiveData<List<Aktivitas>> get() = _aktivitasList

    init {
        loadProjects()
    }

    fun loadProjects() {
        viewModelScope.launch {
            _projects.value = projectDao.getAllProjects()
        }
    }

    fun loadPekerjaans(projectId: Int) {
        viewModelScope.launch {
            _pekerjaans.value = pekerjaanDao.getPekerjaansByProjectId(projectId)
        }
    }

    fun loadAktivitas(pekerjaanId: Int) {
        viewModelScope.launch {
            _aktivitasList.value = aktivitasDao.getAktivitasByPekerjaanId(pekerjaanId)
        }
    }

    fun getAktivitasByPekerjaanId(pekerjaanId: Int) {
        viewModelScope.launch {
            _aktivitasList.value = aktivitasDao.getAktivitasByPekerjaanId(pekerjaanId)
        }
    }

    fun insertProject(project: Project) {
        viewModelScope.launch {
            projectDao.insertProject(project)
            loadProjects()
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch {
            projectDao.updateProject(project)
            loadProjects()
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            projectDao.deleteProject(project)
            loadProjects()
        }
    }

    fun insertPekerjaan(pekerjaan: Pekerjaan) {
        viewModelScope.launch {
            pekerjaanDao.insertPekerjaan(pekerjaan)
            loadPekerjaans(pekerjaan.projectId)
        }
    }

    fun updatePekerjaan(pekerjaan: Pekerjaan) {
        viewModelScope.launch {
            pekerjaanDao.updatePekerjaan(pekerjaan)
            loadPekerjaans(pekerjaan.projectId)
        }
    }

    fun deletePekerjaan(pekerjaan: Pekerjaan) {
        viewModelScope.launch {
            pekerjaanDao.deletePekerjaan(pekerjaan)
            loadPekerjaans(pekerjaan.projectId)
        }
    }

    fun insertAktivitas(aktivitas: Aktivitas) {
        viewModelScope.launch {
            aktivitasDao.insertAktivitas(aktivitas)
            loadAktivitas(aktivitas.pekerjaanId)
        }
    }

    fun updateAktivitas(aktivitas: Aktivitas) {
        viewModelScope.launch {
            aktivitasDao.updateAktivitas(aktivitas)
            loadAktivitas(aktivitas.pekerjaanId)
        }
    }

    fun deleteAktivitas(aktivitas: Aktivitas) {
        viewModelScope.launch {
            aktivitasDao.deleteAktivitas(aktivitas)
            loadAktivitas(aktivitas.pekerjaanId)
        }
    }

    suspend fun getProjectById(projectId: Int): Project {
        return projectDao.getProjectById(projectId)
    }

    suspend fun getPekerjaanById(pekerjaanId: Int): Pekerjaan {
        return pekerjaanDao.getPekerjaanById(pekerjaanId)
    }

    suspend fun getAktivitasById(aktivitasId: Int): Aktivitas {
        return aktivitasDao.getAktivitasById(aktivitasId)
    }
}

class ProjectViewModelFactory(private val appDatabase: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectViewModel(appDatabase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

