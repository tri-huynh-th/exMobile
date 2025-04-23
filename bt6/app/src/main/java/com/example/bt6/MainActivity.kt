package com.example.bt6

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bt6.data.api.RetrofitClient
import com.example.bt6.data.repository.TaskRepository
import com.example.bt6.ui.screens.BottomBar
import com.example.bt6.ui.screens.appScreen.AddNewTaskScreen
import com.example.bt6.ui.screens.appScreen.AppScreenOne
import com.example.bt6.ui.screens.appScreen.DetailScreen
import com.example.bt6.viewmodel.TaskViewModel
import com.example.bt6.viewmodel.TaskViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TaskApp()
                }
            }
        }
    }
}

@Composable
fun TaskApp() {
    val navController = rememberNavController()
    val repository = remember { TaskRepository(RetrofitClient.taskApiService) }
    val taskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(repository)
    )
    
    // Kiểm tra hiển thị BottomBar dựa vào route hiện tại
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute != "onboarding" 
                     && currentRoute != "detail_screen/{taskId}"
    
    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomBar(navController = navController)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AppNavHost(
                navController = navController,
                taskViewModel = taskViewModel
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    taskViewModel: TaskViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "app_screen_one"
    ) {
        composable("app_screen_one") {
            AppScreenOne(
                navController = navController,
                taskViewModel = taskViewModel,
                onTaskClick = { taskId ->
                    navController.navigate("detail_screen/$taskId")
                }
            )
        }
        
        composable(
            route = "detail_screen/{taskId}",
            arguments = listOf(navArgument("taskId") { type = NavType.IntType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getInt("taskId") ?: -1
            DetailScreen(
                navController = navController,
                taskId = taskId,
                taskViewModel = taskViewModel
            )
        }
        
        composable("add_new_task") {
            AddNewTaskScreen(
                navController = navController,
                taskViewModel = taskViewModel
            )
        }
        
        // Các màn hình khác (tạm thời hiển thị placeholders)
        composable("calendar_screen") {
            PlaceholderScreen(stringResource(R.string.calendar))
        }
        
        composable("notes_screen") {
            PlaceholderScreen(stringResource(R.string.notes))
        }
        
        composable("settings_screen") {
            PlaceholderScreen(stringResource(R.string.settings))
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = "$title\n${stringResource(R.string.coming_soon)}",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
    }
}