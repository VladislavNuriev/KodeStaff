package com.example.kodestaff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.data.models.Department
import com.example.profile.models.EmployeeUi
import com.example.profile.ui.ProfileScreen
import com.example.ui.KodeStaffTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KodeStaffTheme {
                ProfileScreen(onBackClicked = {},
                    employee = EmployeeUi(
                        "1",
                        "avatarUrl",
                        "John",
                        "Doe",
                        "JD",
                        department = Department.Design,
                        position = "Strategist",
                        "5 июн",
                        phone = "899"
                    ))
            }
        }
    }
}