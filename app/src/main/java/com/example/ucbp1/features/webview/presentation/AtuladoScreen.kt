package com.example.ucbp1.features.webview.presentation

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeoutOrNull
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun AtuladoScreen(url: String,
                  postData: String?,
                  shouldStopBrowsing: (String?) -> Boolean,
                  modifier: Modifier){

    val webView = remember { mutableStateOf<WebView?>(null) }
    var canGoBack by remember { mutableStateOf(false) }
    var navigateBack by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(true) }
    var showTimeoutDialog by remember { mutableStateOf(false) }
    val pageLoadTrigger = remember { mutableStateOf(0) }

    LaunchedEffect(key1 = pageLoadTrigger.value) {
        if (isLoading) {
            val result = withTimeoutOrNull(1000L) {
                while (isLoading) {
                    delay(100)
                }
                "Success"
            }
            if (result == null) {
                showTimeoutDialog = true
            }
        }
    }
    BackHandler(enabled = canGoBack) {
        navigateBack = true
    }

    LaunchedEffect(navigateBack) {
        if (navigateBack) {
            val currentWebView = webView.value

            if (currentWebView != null && currentWebView.canGoBack()) {
                currentWebView.goBack()
            }
        }
        navigateBack = false
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Onboarding")
                },
                navigationIcon = {
                    if(canGoBack) {
                        IconButton(
                            onClick = {
                                navigateBack = true
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Atrás" // Descripción para accesibilidad
                            )
                        }
                    }
                }
            )
        },

        content = { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AndroidView(
                    modifier = modifier.fillMaxSize(),
                    factory = { context ->
                        WebView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )

                            settings.apply {
                                loadWithOverviewMode = true
                                isFocusable = true
                                isFocusableInTouchMode = true
                                useWideViewPort = true
                                javaScriptEnabled = true
                                cacheMode = WebSettings.LOAD_NO_CACHE
                            }

                            webViewClient = object : WebViewClient() {
                                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                                    super.onPageStarted(view, url, favicon)
                                    isLoading = true
                                    pageLoadTrigger.value++
                                }

                                override fun onPageFinished(view: WebView?, url: String?) {
                                    super.onPageFinished(view, url)
                                    isLoading = false
                                    canGoBack = view?.canGoBack() == true
                                }

                                override fun shouldOverrideUrlLoading(
                                    view: WebView?,
                                    request: WebResourceRequest?,
                                ): Boolean {
                                    return if (shouldStopBrowsing(request?.url.toString())) true
                                    else super.shouldOverrideUrlLoading(view, request)
                                }

                                override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                                    super.doUpdateVisitedHistory(view, url, isReload)
                                    canGoBack = view?.canGoBack() == true
                                }
                            }
                            if (postData != null) {
                                postUrl(url, postData.toByteArray(StandardCharsets.UTF_8))
                            } else {
                                loadUrl(url)
                            }
                            webView.value = this
                        }
                    },
                )
                if (isLoading && !showTimeoutDialog) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    )

    if (showTimeoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showTimeoutDialog = false
                isLoading = false // Detenemos la carga visual
            },
            title = { Text("Tiempo de espera excedido") },
            text = { Text("La página está tardando demasiado en responder. Por favor, verifica tu conexión a internet o inténtalo más tarde.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTimeoutDialog = false
                        isLoading = false // Detenemos la carga visual
                        webView.value?.stopLoading() // Detenemos la carga real del WebView
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}