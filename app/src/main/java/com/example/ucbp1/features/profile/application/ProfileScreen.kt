package com.example.ucbp1.features.profile.application

// --- 1. IMPORTS NECESARIOS PARA LA CÁMARA Y PERMISOS ---
import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable // Para hacer la imagen clickeable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // Para obtener el contexto
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil3.compose.AsyncImage
import org.koin.androidx.compose.koinViewModel
import java.io.File
import java.io.FileOutputStream


@Composable
fun ProfileScreen(
    profileViewModel: ProfileViewModel = koinViewModel()
) {
    val state = profileViewModel.state.collectAsState()
    val context = LocalContext.current // Contexto necesario para la cámara

    // --- ESTADO PARA LA URI DE LA IMAGEN NUEVA ---
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    // --- URI TEMPORAL PARA LA CÁMARA ---
    val tempCameraUri = remember { getTempImageUri(context) }

    // --- LANZADOR PARA LA CÁMARA ---
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                // Si la foto se tomó con éxito, la guardamos en la galería y actualizamos la UI
                val savedUri = saveImageToGallery(context, tempCameraUri)
                imageUri = savedUri
            }
        }
    )

    // --- LANZADOR PARA PEDIR PERMISOS DE CÁMARA ---
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                // Si se concede el permiso, lanzamos la cámara.
                cameraLauncher.launch(tempCameraUri)
            } else {
                // Opcional: Mostrar un mensaje si el permiso es denegado.
            }
        }
    )

    LaunchedEffect(Unit) {
        profileViewModel.loadProfileData()
    }

    // --- ESTADO Y COLORES PARA EL MODO OSCURO LOCAL ---
    var isDarkMode by remember { mutableStateOf(false) }
    val backgroundColor = if (isDarkMode) Color.Black else Color.White
    val textColor = if (isDarkMode) Color.White else Color.Black
    val cardBackgroundColor = if (isDarkMode) Color(0xFF2E2E2E) else Color.White
    val dividerColor = if (isDarkMode) Color.DarkGray else Color.LightGray

    val profileState = state.value

    // --- LÓGICA DE LA IMAGEN A MOSTRAR ---
    val displayImage: Any? = imageUri ?: profileState.avatarUrl

    when {
        profileState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize().background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        profileState.error != null -> {
            Column(modifier = Modifier.fillMaxSize().background(backgroundColor)) {
                Text(
                    text = profileState.error,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        else -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // SECCIÓN DEL AVATAR (AHORA CLICKEABLE)
                AsyncImage(
                    model = displayImage, // Usar la imagen dinámica
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                        .clickable { // --- HACER LA IMAGEN CLICKEABLE ---
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        },
                    contentScale = ContentScale.Crop
                )

                // --- INICIO DE LA UI RESTAURADA ---

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = profileState.userName,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = textColor // Aplicar color de texto
                )

                Spacer(modifier = Modifier.height(24.dp))

                // TARJETA CON LA NUEVA INFORMACIÓN
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = cardBackgroundColor) // Aplicar color de tarjeta
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (profileState.dollarValue != null) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Isaac el Valor del Dólar en (Bs) esta:",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isDarkMode) Color.LightGray else Color.DarkGray
                                )
                                Text(
                                    text = "%.2f".format(profileState.dollarValue),
                                    style = MaterialTheme.typography.headlineMedium,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Divider(modifier = Modifier.padding(vertical = 16.dp), color = dividerColor)
                        }
                        InfoRow(
                            icon = Icons.Default.Email,
                            text = profileState.userEmail,
                            textColor = textColor
                        )
                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = dividerColor)
                        InfoRow(
                            icon = Icons.Default.School,
                            text = "Ingeniería de Sistemas",
                            textColor = textColor
                        )
                        Divider(modifier = Modifier.padding(vertical = 12.dp), color = dividerColor)
                        InfoRow(
                            icon = Icons.Default.Apartment,
                            text = "Campus La Paz",
                            textColor = textColor
                        )
                    }
                }

                // SECCIÓN DEL BOTÓN DE MODO OSCURO
                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Modo Oscuro",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = textColor
                    )
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { isDarkMode = it }
                    )
                }
                // --- FIN DE LA UI RESTAURADA ---
            }
        }
    }
}

// --- FUNCIONES AUXILIARES PARA MANEJAR ARCHIVOS E IMÁGENES ---

private fun getTempImageUri(context: Context): Uri {
    val tempFile = File.createTempFile("ucbp1_temp_avatar", ".jpg", context.cacheDir).apply {
        createNewFile()
    }
    return FileProvider.getUriForFile(context, "${context.packageName}.provider", tempFile)
}

private fun saveImageToGallery(context: Context, uri: Uri): Uri? {
    val contentResolver = context.contentResolver
    val bitmap = context.contentResolver.openInputStream(uri)?.use {
        android.graphics.BitmapFactory.decodeStream(it)
    } ?: return null

    val values = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, "ucbp1_avatar_${System.currentTimeMillis()}.jpg")
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.IS_PENDING, 1)
        }
    }

    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    } else {
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    }
    val newImageUri = contentResolver.insert(collection, values)

    newImageUri?.let {
        contentResolver.openOutputStream(it).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out!!)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.clear()
            values.put(MediaStore.Images.Media.IS_PENDING, 0)
            contentResolver.update(it, values, null, null)
        }
    }
    return newImageUri
}

// Modificamos InfoRow para que acepte un color de texto
@Composable
private fun InfoRow(icon: ImageVector, text: String, textColor: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 16.sp,
            color = textColor
        )
    }
}
