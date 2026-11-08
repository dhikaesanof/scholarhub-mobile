package com.example.scholarhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.scholarhub.data.scholarship.LocalScholarshipRepository
import com.example.scholarhub.feature.scholarship.detail.ScholarshipDetailScreen
import com.example.scholarhub.feature.scholarship.detail.ScholarshipDetailViewModel
import com.example.scholarhub.ui.theme.ScholarHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.light(0xFFF1F1F1.toInt(), 0xFFF1F1F1.toInt()))
        val repository = LocalScholarshipRepository(assets)
        val scholarshipId = intent.getStringExtra("scholarshipId") ?: "SH28"
        val factory = viewModelFactory {
            initializer { ScholarshipDetailViewModel(scholarshipId, repository) }
        }
        val model = ViewModelProvider(this, factory)[
            "scholarship-detail:$scholarshipId",
            ScholarshipDetailViewModel::class.java,
        ]
        setContent {
            ScholarHubTheme {
                ScholarshipDetailScreen(
                    model = model,
                    onBack = ::finish,
                )
            }
        }
    }
}
