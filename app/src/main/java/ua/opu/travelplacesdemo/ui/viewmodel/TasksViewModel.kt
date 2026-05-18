package ua.opu.travelplacesdemo.ui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ua.opu.travelplacesdemo.data.TaskItem
import ua.opu.travelplacesdemo.data.TaskPriority

class TasksViewModel : ViewModel() {
    private val _tasks = MutableStateFlow<List<TaskItem>>(
        listOf(
            TaskItem(title = "Підготовка презентації", description = "Підготувати слайди та матеріали", priority = TaskPriority.HIGH),
            TaskItem(title = "Написати звіт", description = "Звіт за поточний тиждень", priority = TaskPriority.MEDIUM),
            TaskItem(title = "Оновити контакти", description = "Можливо пізніше додати нові мейли", priority = TaskPriority.LOW)
        )
    )
    val tasks: StateFlow<List<TaskItem>> = _tasks.asStateFlow()

    fun getTaskById(id: String?): TaskItem? {
        return _tasks.value.find { it.id == id }
    }

    fun addOrUpdateTask(id: String?, title: String, description: String, priority: TaskPriority) {
        if (title.isBlank()) return
        _tasks.update { currentList ->
            if (id != null && currentList.any { it.id == id }) {
                currentList.map {
                    if (it.id == id) it.copy(title = title, description = description, priority = priority) else it
                }
            } else {
                currentList + TaskItem(title = title, description = description, priority = priority)
            }
        }
    }

    fun toggleTaskCompletion(id: String) {
        _tasks.update { currentList ->
            currentList.map {
                if (it.id == id) it.copy(isCompleted = !it.isCompleted) else it
            }.toList()
        }
    }

    fun deleteTask(id: String) {
        _tasks.update { currentList -> currentList.filter { it.id != id } }
    }
}