package com.example.spellcastingcompanion.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import com.example.spellcastingcompanion.ui.tasks.TasksScreen

// Import your screen Composable functions here once they are created
// e.g., import com.yourusername.spellcasting.ui.homescreen.HomeScreen
// ... etc.

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            // HomeScreen(navController = navController) // Placeholder
            // For now, let's navigate to TasksScreen from Home for testing
            // In a real app, HomeScreen would have its own UI

            Text("Home Screen - Navigate to Tasks to see content",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.clickable { navController.navigate(Screen.Tasks.route) })
        }
        composable(Screen.Tasks.route) {
            TasksScreen(navController = navController) // Use your actual screen
        }
        composable(Screen.Projects.route) {
            // ProjectsScreen(navController = navController) // Placeholder
        }
        composable(Screen.NewTask.route) {
            // NewTaskScreen(navController = navController) // Placeholder
        }
        composable(Screen.NewProject.route) {
            // NewProjectScreen(navController = navController) // Placeholder
        }
        composable(
            route = Screen.TaskDetails.route,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            // TaskDetailsScreen(navController = navController, taskId = taskId) // Placeholder
        }
        composable(
            route = Screen.ProjectDetails.route,
            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
        ) { backStackEntry ->
            val projectId = backStackEntry.arguments?.getString("projectId")
            // ProjectDetailsScreen(navController = navController, projectId = projectId) // Placeholder
        }
    }
}