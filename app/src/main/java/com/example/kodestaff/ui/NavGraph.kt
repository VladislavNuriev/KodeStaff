package com.example.kodestaff.ui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.employees.ui.EmployeesScreen
import com.example.profile.ui.ProfileScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Employees.route
    ) {
        composable(Screen.Employees.route) {
            EmployeesScreen(
                onEmployeeClick = { employee ->
                    navController.navigate(Screen.Profile.createRoute(employee.id))
                }
            )
        }
        composable(Screen.Profile.route) {
            val employeeId = Screen.Profile.getEmployeeId(it.arguments)
            ProfileScreen(
                employeeId = employeeId,
                onFinished = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    data object Employees : Screen("employees")
    data object Profile : Screen("profile/{employee_id}") {
        fun createRoute(employeeId: String): String {
            return "profile/$employeeId"
        }

        fun getEmployeeId(arguments: Bundle?): String {
            return arguments?.getString("employee_id") ?: ""
        }
    }

}