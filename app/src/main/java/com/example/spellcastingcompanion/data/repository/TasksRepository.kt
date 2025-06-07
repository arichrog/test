package com.example.spellcastingcompanion.data.repository

import com.example.spellcastingcompanion.SpellcastingApplication
import com.example.spellcastingcompanion.data.model.Category
import com.example.spellcastingcompanion.data.model.Task
import com.example.spellcastingcompanion.data.model.Priority
import com.example.spellcastingcompanion.ui.tasks.TaskFilterStatus // Ensure this import is correct based on your file structure
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

// Sealed class for handling results from the repository
sealed class RepositoryResult<out T> {
    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class Error(val message: String, val exception: Exception? = null) : RepositoryResult<Nothing>()
}

// DTOs (Data Transfer Objects)
@Serializable
data class CategoryDto(
    // Use @SerialName if your Supabase column names are snake_case e.g. @SerialName("icon_name")
    @SerialName("id") val id: String,
    @SerialName("name") val name: String,
    @SerialName("iconName") val iconName: String,
    @SerialName("colorName") val colorName: String
)

@Serializable
data class TaskDto(
    @SerialName("id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String?,
    // Use @SerialName("category_id") if your Supabase column is "category_id"
    @SerialName("categoryId") val categoryId: String,
    @SerialName("completed") val completed: Boolean,
    // Use @SerialName("created_at") if your Supabase column is "created_at"
    @SerialName("createdAt") val createdAt: String, // ISO 8601 String from Supabase
    // Use @SerialName("due_date") if your Supabase column is "due_date"
    @SerialName("dueDate") val dueDate: String?,    // Nullable ISO 8601 String
    @SerialName("priority") val priority: String   // "LOW", "MEDIUM", "HIGH"
)

class TasksRepository {

    private val supabasePostgrest: Postgrest
        get() = SpellcastingApplication.supabaseClient.postgrest

    // Helper to convert ISO String timestamp to Long (milliseconds)
    private fun isoToEpochMillis(isoString: String?): Long? {
        return isoString?.let {
            try {
                // Pattern for "YYYY-MM-DDTHH:MM:SS+ZZ:ZZ" or "YYYY-MM-DDTHH:MM:SSZ" (UTC)
                // If Supabase includes milliseconds like ".123Z", use ".SSSX"
                val primaryFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ROOT)
                primaryFormat.timeZone = TimeZone.getTimeZone("UTC") // Assume UTC, Supabase timestamptz is good at this
                primaryFormat.parse(it)?.time
            } catch (e: java.text.ParseException) {
                try {
                    // Fallback for format with milliseconds: "YYYY-MM-DDTHH:MM:SS.SSSX"
                    val fallbackFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ROOT)
                    fallbackFormat.timeZone = TimeZone.getTimeZone("UTC")
                    fallbackFormat.parse(it)?.time
                } catch (e2: java.text.ParseException) {
                    // Log e2 for debugging if needed
                    println("Error parsing date string: $isoString. Error: ${e2.message}")
                    null
                }
            } catch (e: Exception) {
                // Log e for debugging if needed
                println("Generic error parsing date string: $isoString. Error: ${e.message}")
                null
            }
        }
    }

    suspend fun getAllCategories(): RepositoryResult<List<Category>> {
        return try {
            // Ensure your table name in Supabase is "categories"
            val dtoList = supabasePostgrest.from("categories").select().decodeList<CategoryDto>()
            val categories = dtoList.map { dto ->
                Category(
                    id = dto.id,
                    name = dto.name,
                    iconName = dto.iconName,
                    colorName = dto.colorName
                )
            }
            RepositoryResult.Success(categories)
        } catch (e: Exception) {
            println("Error fetching categories: ${e.message}")
            RepositoryResult.Error("Failed to fetch categories: ${e.message}", e)
        }
    }

    suspend fun getTasks(
        categoryId: String? = null,
        filterStatus: TaskFilterStatus? = null // From com.example.spellcastingcompanion.ui.tasks
    ): RepositoryResult<List<Task>> {
        return try {
            // Ensure your table name in Supabase is "tasks"
            val query = supabasePostgrest.from("tasks").select()

            categoryId?.let { query.eq("categoryId", it) } // Assumes 'categoryId' column name in DB

            when (filterStatus) {
                TaskFilterStatus.ACTIVE -> query.eq("completed", false.toString())
                TaskFilterStatus.COMPLETED -> query.eq("completed", true.toString())
                TaskFilterStatus.ALL, null -> { /* No completion filter needed */ }
            }

            query.order("createdAt", Order.DESCENDING) // Assumes 'createdAt' column name

            val dtoList = query.decodeList<TaskDto>()
            val tasks = dtoList.mapNotNull { dto ->
                try {
                    val taskPriority = try {
                        Priority.valueOf(dto.priority.uppercase(Locale.ROOT))
                    } catch (e: IllegalArgumentException) {
                        println("Invalid priority string from DB: ${dto.priority} for task ${dto.id}")
                        Priority.LOW // Default or handle error appropriately
                    }
                    Task(
                        id = dto.id,
                        title = dto.title,
                        description = dto.description ?: "",
                        categoryId = dto.categoryId,
                        completed = dto.completed,
                        createdAt = isoToEpochMillis(dto.createdAt)
                            ?: System.currentTimeMillis(), // Fallback or handle error
                        dueDate = isoToEpochMillis(dto.dueDate),
                        priority = taskPriority
                    )
                } catch (e: Exception) {
                    println("Error mapping TaskDto to Task for id ${dto.id}: ${e.message}")
                    null // Skip this task if critical mapping fails
                }
            }
            RepositoryResult.Success(tasks)
        } catch (e: Exception) {
            println("Error fetching tasks: ${e.message}")
            RepositoryResult.Error("Failed to fetch tasks: ${e.message}", e)
        }
    }

    suspend fun addTask(task: Task): RepositoryResult<Task> {
        // TODO: Implement insert into Supabase "tasks" table
        // 1. Create a TaskInsertDto or ensure Task is suitable for direct serialization.
        //    - Convert Long timestamps (createdAt, dueDate) to ISO 8601 strings.
        //    - Convert Priority enum to its String name (e.g., task.priority.name).
        // 2. Perform the insert operation using supabasePostgrest.
        // 3. Decode the result (Supabase usually returns the inserted row(s)).
        // 4. Map the result DTO back to your Task domain model.
        // Conceptual Example:
        // @Serializable
        // data class TaskInsertDto(
        //     @SerialName("title") val title: String,
        //     @SerialName("description") val description: String?,
        //     @SerialName("category_id") val categoryId: String, // Example if DB is snake_case
        //     @SerialName("completed") val completed: Boolean,
        //     @SerialName("created_at") val createdAt: String, // ISO String
        //     @SerialName("due_date") val dueDate: String?, // Nullable ISO String
        //     @SerialName("priority") val priority: String
        // )
        // val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX", Locale.ROOT).apply { timeZone = TimeZone.getTimeZone("UTC") }
        // val taskToInsert = TaskInsertDto(
        //     title = task.title,
        //     description = task.description.ifEmpty { null },
        //     categoryId = task.categoryId,
        //     completed = task.completed,
        //     createdAt = dateFormat.format(Date(task.createdAt)),
        //     dueDate = task.dueDate?.let { dateFormat.format(Date(it)) },
        //     priority = task.priority.name
        // )
        // try {
        //     // Assuming `id` is generated by DB or you want to insert it too if it's client-generated
        //     val insertedDto = supabasePostgrest.from("tasks").insert(taskToInsert).decodeSingle<TaskDto>()
        //     // Then map insertedDto back to Task domain model (similar to getTasks)
        //     // return RepositoryResult.Success(mappedTask)
        // } catch (e: Exception) {
        //     return RepositoryResult.Error("Failed to add task: ${e.message}", e)
        // }
        println("addTask called but not fully implemented.")
        return RepositoryResult.Error("Add task not implemented yet.")
    }

    suspend fun updateTask(task: Task): RepositoryResult<Task> {
        // TODO: Implement update in Supabase "tasks" table
        println("updateTask called but not fully implemented.")
        return RepositoryResult.Error("Update task not implemented yet.")
    }

    suspend fun deleteTask(taskId: String): RepositoryResult<Unit> {
        // TODO: Implement delete from Supabase "tasks" table
        println("deleteTask called but not fully implemented.")
        return RepositoryResult.Error("Delete task not implemented yet.")
    }
}

private fun PostgrestResult.order(
    string: String,
    order: Order
) {
}

private fun PostgrestResult.eq(
    string: String,
    toString: String,
) {
}
