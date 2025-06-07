package com.example.spellcastingcompanion.ui.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.lazy.LazyColumn // Will use later for task list
// import androidx.compose.foundation.lazy.items // Will use later
import androidx.compose.material3.Button // For filter buttons
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.spellcastingcompanion.ui.theme.SpellcastingTheme
import com.example.spellcastingcompanion.ui.theme.MedievalSharp
import com.example.spellcastingcompanion.data.model.Category // For CategoryItem

// Placeholder for CategoryItem - you'll need to implement this based on your IngredientItem.tsx
// For now, just a simple clickable text
@Composable
fun CategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit,
    count: Int // Task count for this category
) {
    Column(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // TODO: Implement a proper visual representation like your IngredientItem.tsx
        // This would include an icon and background color based on the category.
        // For now, using text.
        Text(
            text = "${category.name} ($count)",
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    navController: NavController,
    viewModel: TasksViewModel = viewModel() // Instantiate the ViewModel
) {
    val uiState by viewModel.uiState.collectAsState() // Observe the UI state

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Component Catalog",
                        fontFamily = MedievalSharp,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
                // Add "Clear" filter button similar to your web app
                // actions = {
                //     TextButton(onClick = { viewModel.selectCategory(null) }) {
                //         Text("Clear")
                //     }
                // }
            )
        }
        // FAB for adding new task, can be added later
        // floatingActionButton = { ... }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Filter by School (Categories) - based on your web app's LeftPage
            Text("Filter By School", style = MaterialTheme.typography.titleMedium, fontFamily = MedievalSharp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround // Or use a LazyRow or FlowRow for more items
            ) {
                uiState.categories.forEach { category ->
                    // TODO: Calculate actual task count for this category
                    val taskCount = uiState.tasks.count { it.categoryId == category.id }
                    CategoryItem(
                        category = category,
                        isSelected = uiState.selectedCategoryId == category.id,
                        onClick = { viewModel.selectCategory(category.id) },
                        count = taskCount
                    )
                }
            }
            if (uiState.selectedCategoryId != null) {
                Button(onClick = { viewModel.selectCategory(null) }, modifier = Modifier.align(Alignment.End)) {
                    Text("Clear School")
                }
            }


            Spacer(modifier = Modifier.height(16.dp))

            // Status Filters - based on your web app's LeftPage
            Text("Status Filters", style = MaterialTheme.typography.titleMedium, fontFamily = MedievalSharp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TaskFilterStatus.values().forEach { status ->
                    Button(
                        onClick = { viewModel.setFilterStatus(status) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (uiState.filterStatus == status) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = if (uiState.filterStatus == status) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Text(status.name.lowercase().replaceFirstChar { it.titlecase() })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Title for the list section
            val listTitle = uiState.selectedCategoryId?.let { catId ->
                uiState.categories.find { it.id == catId }?.name + " Components"
            } ?: "All Components"
            Text(listTitle, style = MaterialTheme.typography.headlineSmall, fontFamily = MedievalSharp)
            Spacer(modifier = Modifier.height(8.dp))


            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                if (uiState.tasks.isEmpty()) {
                    Text(
                        text = "No ingredients in your alchemy pouch yet.\nCreate new tasks to begin brewing!", // Message from web app
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    // TODO: Display tasks in a LazyColumn
                    // LazyColumn {
                    // items(uiState.tasks, key = { task -> task.id }) { task ->
                    // TaskItem(task = task, onTaskClick = { /* navigate to details */ }, onCheckedChange = { /* update completion */ })
                    //     }
                    // }
                    Text("Tasks would be listed here. Count: ${uiState.tasks.size}") // Placeholder
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun TasksScreenPreview() {
    SpellcastingTheme {
        TasksScreen(navController = rememberNavController())
    }
}