package com.example.bookwave

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ShowPdf : AppCompatActivity() {


    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_show_pdf)

        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                )

        val pdfUrl = intent.getStringExtra("PDF_URL")

        webView = findViewById(R.id.pdf_webview)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = WebViewClient()

        pdfUrl?.let {
            val googleDocsUrl = "https://docs.google.com/viewer?url=${Uri.encode(it)}"
            webView.loadUrl(googleDocsUrl)
        } ?: run {
            Log.e("PdfViewActivity", "PDF URL is null")
        }
    }
}