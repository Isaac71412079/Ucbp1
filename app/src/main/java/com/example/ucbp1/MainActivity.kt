package com.example.ucbp1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.ucbp1.features.github.presentation.GithubScreen
import com.example.ucbp1.navigation.AppNavigation
import com.example.ucbp1.navigation.NavigationDrawer
import com.example.ucbp1.navigation.NavigationViewModel
import com.example.ucbp1.navigation.Screen
import com.example.ucbp1.ui.theme.Ucbp1Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import com.example.ucbp1.features.login.presentation.LoginScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Ucbp1Theme {
                val rootNavController = rememberNavController()
                NavHost(
                    navController = rootNavController,
                    startDestination = Screen.Login.route // La app siempre empieza en Login
                ) {
                    // Destino 1: La pantalla de Login
                    composable(Screen.Login.route) {
                        LoginScreen(
                            onLoginSuccess = {
                                // Al tener éxito, navega a todo el flujo principal
                                rootNavController.navigate("main_flow") {
                                    // Limpia el stack para que el usuario no pueda volver al Login
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }

                    // Destino 2: Todo el flujo de la app principal con el Drawer
                    composable("main_flow") {
                        // Llamamos a MainApp, que ahora contendrá el Drawer y el NavHost interno
                        MainApp(navigationViewModel = koinViewModel())
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NavigationDrawerHost(
        coroutineScope: CoroutineScope,
        drawerState: DrawerState,
        navigationViewModel: NavigationViewModel,
        navController: NavHostController
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(stringResource(R.string.app_name)) },
                    modifier = Modifier.statusBarsPadding(),
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu"
                            )
                        }
                    }
                )
            }
        ) { innerPadding ->
            // Llama a tu NavHost principal, pasándole el padding
            AppNavigation(
                navController = navController,
                navigationViewModel = navigationViewModel,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun MainApp(navigationViewModel: NavigationViewModel) {
        val navController: NavHostController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination

        val navigationDrawerItems = listOf(
            NavigationDrawer.Profile,
            NavigationDrawer.Dollar,
            NavigationDrawer.Movie,
            NavigationDrawer.Github
        )
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val coroutineScope = rememberCoroutineScope()

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(256.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(), // Usar fillMaxWidth para centrar correctamente
                        contentAlignment = Alignment.Center
                    ) {
                        // Nota: Asegúrate de tener estas imágenes en tu carpeta res/drawable
                        // Si no las tienes, puedes comentar estas líneas por ahora
                        /*
                        Image(
                            modifier = Modifier.width(120.dp),
                            painter = painterResource(id = R.drawable.ic_launcher_background),
                            contentDescription = "Logo Background",
                        )
                        Image(
                            painter = painterResource(id = R.drawable.ic_launcher_foreground),
                            contentDescription = "Logo Foreground",
                            modifier = Modifier.padding(16.dp)
                        )
                        */
                    }
                    Spacer(Modifier.height(16.dp)) // Espacio entre el logo y los items
                    navigationDrawerItems.forEach { item ->
                        val isSelected = currentDestination?.route == item.route
                        NavigationDrawerItem(
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(item.label) },
                            selected = isSelected,
                            onClick = {
                                // En lugar de usar navController directamente, usamos el ViewModel
                                // para mantener la lógica centralizada, aunque la opción directa
                                // también funciona como en las instrucciones.
                                // Opción 1 (Directa): navController.navigate(...)
                                // Opción 2 (Con ViewModel):
                                navigationViewModel.navigateTo(
                                    item.route,
                                    NavigationViewModel.NavigationOptions.REPLACE_HOME
                                )

                                coroutineScope.launch {
                                    drawerState.close()
                                }
                            }
                        )
                    }
                }
            }
        ) {
            // Contenido principal de la app
            NavigationDrawerHost(coroutineScope, drawerState, navigationViewModel, navController)

        }
    }
}