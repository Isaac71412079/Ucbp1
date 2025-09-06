package com.example.ucbp1.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun GithubScreen(modifier: Modifier) {

    var nickname by remember { mutableStateOf("") }

    Column {
        Text( "")
        OutlinedTextField(
            value = nickname,
            onValueChange = {
                it -> nickname = it
            }
        )
        OutlinedButton( onClick = {

        }) {
            Text("")
        }
    }
}