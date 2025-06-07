package com.example.spellcastingcompanion.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spellcastingcompanion.data.model.Task // Your data model
import com.example.spellcastingcompanion.data.model.Category // Your data model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

// This class will hold the UI state for the TasksScreen
data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val categories: List<Category> = emptyList(), // From your web app's filter by school
    val isLoading: Boolean = false,
    val selectedCategoryId: String? = null,
    val filterStatus: TaskFilterStatus = TaskFilterStatus.ALL // ALL, ACTIVE, COMPLETED
    // Add other UI state properties as needed, e.g., error messages
)

enum class TaskFilterStatus {
    ALL, ACTIVE, COMPLETED
}

class TasksViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState(isLoading = true))
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            // TODO: Replace with actual data loading from a Repository (which will use Room)
            // For now, let's simulate loading with some placeholder data
            kotlinx.coroutines.delay(1000) // Simulate network/db delay

            // Placeholder categories based on defaultCategories in your taskStore.ts
            val placeholderCategories = listOf(
                Category(id = "cat-arcane", name = "Arcane", iconName = "sparkles", colorName = "magic_arcane"),
                Category(id = "cat-elemental", name = "Elemental", iconName = "flame", colorName = "magic_red"),
                Category(id = "cat-nature", name = "Nature", iconName = "leaf", colorName = "magic_green"),
                Category(id = "cat-divine", name = "Divine", iconName = "sun", colorName = "magic_yellow")
            )

            // Placeholder tasks (empty for now, will be populated from Room later)
            val placeholderTasks = emptyList<Task>()

            _uiState.value = TasksUiState(
                tasks = placeholderTasks,
                categories = placeholderCategories,
                isLoading = false
            )
        }
    }

    fun selectCategory(categoryId: String?) {
        _uiState.value = _uiState.value.copy(selectedCategoryId = categoryId)
        // In a real app, you'd likely re-filter or re-fetch tasks here
        // based on the selected category and current filterStatus
        filterTasks()
    }

    fun setFilterStatus(status: TaskFilterStatus) {
        _uiState.value = _uiState.value.copy(filterStatus = status)
        // Re-filter or re-fetch tasks
        filterTasks()
    }

    private fun filterTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // TODO: Implement actual task filtering based on _uiState.value.selectedCategoryId
            // and _uiState.value.filterStatus by querying your repository/Room.
            // For now, just simulating a reload.
            kotlinx.coroutines.delay(500) // Simulate filtering delay

            // This is where you'd get the filtered list from your data source.
            // val filteredTasks = repository.getTasks(
            //    category = _uiState.value.selectedCategoryId,
            //    status = _uiState.value.filterStatus
            // )
            // _uiState.value = _uiState.value.copy(tasks = filteredTasks, isLoading = false)

            // For now, we'll just keep the tasks empty and turn off loading.
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    // Add other functions to handle user interactions:
    // - onTaskCheckedChanged(task: Task, isCompleted: Boolean)
    // - onTaskClicked(task: Task) -> navigate to details
    // - onAddNewTaskClicked() -> navigate to new task screen
}