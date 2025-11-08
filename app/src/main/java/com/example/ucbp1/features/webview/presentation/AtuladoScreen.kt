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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
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

    BackHandler(enabled = true) {

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

            AndroidView(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                factory = { it ->
                    WebView(it).apply {
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
                            override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                                view.settings.setSupportZoom(false)
                            }

                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?,
                            ) {
                                super.onReceivedError(view, request, error)

                                println("onReceivedError: ${error?.description}")
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                canGoBack = view?.canGoBack() == true
                                println("onPageFinished: $url")
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

        }
    )

}