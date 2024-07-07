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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.example.projectlab1.ui.theme.ProjectLab1Theme
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: ProjectViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, ProjectViewModelFactory((application as ProjectLab1Application).database)).get(ProjectViewModel::class.java)
        setContent {
            ProjectLab1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun MainScreen(viewModel: ProjectViewModel, modifier: Modifier = Modifier.background(Color.White)) {
    val context = LocalContext.current
    val projectList by viewModel.projects.observeAsStateCustom(emptyList())
    val currentViewModel by rememberUpdatedState(viewModel)

    LaunchedEffect(Unit) {
        currentViewModel.loadProjects()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(color = Color.LightGray)
            ) {
                Text(
                    text = "Projects",
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                Button(onClick = { context.startActivity(Intent(context, ProjectForm::class.java)) }) {
                    Text(text = "Add Project")
                }
            }
        }

        items(projectList) { project ->
            ProjectCard(project = project, viewModel = viewModel)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectCard(project: Project, viewModel: ProjectViewModel) {
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
            Text(text = project.namaProject, fontSize = 20.sp, color = Color.Black)
            Text(text = "PIC: ${project.picProject}", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Status: ${project.statusProject}", fontSize = 16.sp, color = Color.DarkGray)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Button(
                    onClick = {
                        context.startActivity(
                            Intent(context, ProjectDetailScreen::class.java).putExtra("projectId", project.id)
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "View")
                }
                Button(
                    onClick = {
                        viewModel.deleteProject(project)
                        // Refresh the list
                        viewModel.loadProjects()
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
fun <T> LiveData<T>.observeAsStateCustom(initial: T): State<T> {
    val state = remember { mutableStateOf(initial) }
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(this, lifecycleOwner) {
        val observer = Observer<T> { value ->
            state.value = value
        }
        this@observeAsStateCustom.observe(lifecycleOwner, observer)
        onDispose {
            this@observeAsStateCustom.removeObserver(observer)
        }
    }
    return state
}
