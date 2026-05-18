package ua.opu.travelplacesdemo.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable // ВАЖЛИВО: додано імпорт
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ua.opu.travelplacesdemo.ui.screens.AddEditTaskScreen
import ua.opu.travelplacesdemo.ui.screens.DetailsTaskScreen
import ua.opu.travelplacesdemo.ui.screens.TasksListScreen
import ua.opu.travelplacesdemo.ui.viewmodel.TasksViewModel

object TaskRoutes {
    const val LIST = "tasks_list"
    const val ADD_EDIT = "add_edit_task?taskId={taskId}"
    const val DETAILS = "task_details/{taskId}"

    fun navigateToAdd() = "add_edit_task?taskId="
    fun navigateToEdit(id: String) = "add_edit_task?taskId=$id"
    fun navigateToDetails(id: String) = "task_details/$id"
}

@Composable
fun TaskNavigation() {
    val navController = rememberNavController()
    val sharedViewModel: TasksViewModel = viewModel()

    NavHost(navController = navController, startDestination = TaskRoutes.LIST) {
        composable(TaskRoutes.LIST) {
            TasksListScreen(
                viewModel = sharedViewModel,
                onAddTaskClick = { navController.navigate(TaskRoutes.navigateToAdd()) },
                onTaskClick = { id -> navController.navigate(TaskRoutes.navigateToDetails(id)) }
            )
        }

        composable(
            route = TaskRoutes.ADD_EDIT,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType; nullable = true; defaultValue = null })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            AddEditTaskScreen(
                taskId = taskId,
                viewModel = sharedViewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = TaskRoutes.DETAILS,
            arguments = listOf(navArgument("taskId") { type = NavType.StringType })
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            DetailsTaskScreen(
                taskId = taskId,
                viewModel = sharedViewModel,
                onNavigateBack = { navController.popBackStack() },
                onEditClick = { id -> navController.navigate(TaskRoutes.navigateToEdit(id)) },
                onDeleteClick = { navController.popBackStack(TaskRoutes.LIST, false) }
            )
        }
    }
}