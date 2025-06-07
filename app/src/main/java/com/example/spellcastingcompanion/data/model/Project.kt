package com.example.spellcastingcompanion.data.model

// Based on export interface Project from
data class Project(
    val id: String,
    val title: String,
    val description: String,
    val taskIds: List<String>,
    var completed: Boolean,
    val createdAt: Long, // Timestamp
    val dueDate: Long? = null // Nullable timestamp
)