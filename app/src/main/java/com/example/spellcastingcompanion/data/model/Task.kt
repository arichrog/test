package com.example.spellcastingcompanion.data.model

// Based on export interface Task from
data class Task(
    val id: String,
    val title: String,
    val description: String,
    val categoryId: String,
    var completed: Boolean,
    val createdAt: Long, // Timestamp
    val dueDate: Long? = null, // Nullable timestamp
    val priority: Priority
)