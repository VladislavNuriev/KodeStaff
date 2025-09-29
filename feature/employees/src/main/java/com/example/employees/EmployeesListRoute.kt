package com.example.employees

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel

// для тестирования
private const val QUERY_TEST = "er"
private const val DEPARTMENT_TEST = "android"

@Composable
fun EmployeesListScreen(viewModel: EmployeesViewModel = viewModel()) {
    Text(
        text = "Hello Test!"
    )
    Button(onClick = { viewModel.fetchData(QUERY_TEST, DEPARTMENT_TEST) }) {
        Text("Test")
    }
}

