package com.example.ucbp1.features.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ucbp1.R // Asegúrate que sea el R de tu proyecto
import com.example.ucbp1.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // Este LaunchedEffect ejecuta el código de su bloque solo una vez
    // cuando el Composable entra en la composición.
    LaunchedEffect(key1 = true) {
        // Espera 3 segundos (3000 milisegundos)
        delay(3000)
        // Navega a la pantalla de Login y limpia el stack para
        // que el usuario no pueda volver al SplashScreen presionando "atrás".
        navController.popBackStack() // Limpia el splash de la pila
        navController.navigate(Screen.Login.route) // Navega al Login
    }

    // El diseño de la pantalla
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 1. El icono de tu app
        Image(
            painter = painterResource(id = R.mipmap.mobileback4), // Usa el icono de tu app
            contentDescription = "Logo de la App",
            modifier = Modifier.size(150.dp) // Tamaño grande
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 2. El título "ucbp1" debajo
        Text(
            text = "ucbp1",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
