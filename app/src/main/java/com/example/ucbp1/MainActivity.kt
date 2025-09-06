package com.example.ucbp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.ucbp1.presentation.GithubScreen
import com.example.ucbp1.ui.theme.Ucbp1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ucbp1Theme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    GithubScreen( modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
