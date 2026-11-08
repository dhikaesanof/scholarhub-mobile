package com.example.scholarhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.scholarhub.data.scholarship.LocalScholarshipRepository
import com.example.scholarhub.navigation.AppDestination
import com.example.scholarhub.navigation.AppNavGraph
import com.example.scholarhub.ui.theme.ScholarHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(0xFFF1F1F1.toInt(), 0xFFF1F1F1.toInt()))
        val repository = LocalScholarshipRepository(assets)
        val initialDestination = if (intent.hasExtra("scholarshipId")) {
            AppDestination.ScholarshipDetail(intent.getStringExtra("scholarshipId") ?: "SH28")
        } else {
            AppDestination.Login
        }
        setContent {
            ScholarHubTheme {
                AppNavGraph(
                    repository = repository,
                    initialDestination = initialDestination,
                    onFinish = ::finish,
                )
            }
        }
    }
}
