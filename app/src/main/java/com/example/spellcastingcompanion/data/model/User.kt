package com.example.spellcastingcompanion.data.model

// Based on export interface User from
data class User(
    val level: Int,
    val experience: Int,
    val completedTasks: Int,
    val completedProjects: Int
    // Consider adding a userId if you plan for multiple users or cloud sync
)