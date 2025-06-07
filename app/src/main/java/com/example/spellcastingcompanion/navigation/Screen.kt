package com.example.spellcastingcompanion.navigation

// Based on routes in App.tsx
sealed class Screen(val route: String) {
    object Home : Screen("home_screen")
    object Tasks : Screen("tasks_screen")
    object Projects : Screen("projects_screen")
    object NewTask : Screen("new_task_screen")
    object NewProject : Screen("new_project_screen")
    object TaskDetails : Screen("task_details_screen/{taskId}") {
        fun createRoute(taskId: String) = "task_details_screen/$taskId"
    }
    object ProjectDetails : Screen("project_details_screen/{projectId}") {
        fun createRoute(projectId: String) = "project_details_screen/$projectId"
    }
    // Add any other screens you might need
}