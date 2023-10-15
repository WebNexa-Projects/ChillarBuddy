package com.arisu.chillarbuddy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.arisu.chillarbuddy.databinding.ActivityWalletBinding
import com.arisu.chillarbuddy.databinding.ActivityWithVerifyBinding
import java.net.URLDecoder
import java.util.Base64

class withVerifyActivity : AppCompatActivity() {

    lateinit var binding: ActivityWithVerifyBinding
    private var backable = true
    lateinit var email: String

    init {
        System.loadLibrary("keys")
    }

    external fun Hatbc(): String

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityWithVerifyBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.webview2.settings.javaScriptEnabled = true
        binding.webview2.settings.builtInZoomControls = false
        binding.webview2.settings.setSupportZoom(false)
        binding.webview2.settings.displayZoomControls = false
        binding.webview2.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        binding.webview2.settings.loadWithOverviewMode = true
        binding.webview2.settings.useWideViewPort = true

        binding.p.visibility = View.VISIBLE

        binding.webview2.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                // This method will be called as the page loading progresses.
                // You can check the progress value here and take actions accordingly.
                backable = false
                if (newProgress >= 85) {
                    binding.p.visibility = View.GONE
                } else {
                    binding.p.visibility = View.VISIBLE
                }
            }
        }
        binding.webview2.webViewClient = object : WebViewClient() {
            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                backable = true
            }
            @SuppressLint("SuspiciousIndentation")
            override fun onPageFinished(view: WebView?, url: String?) {

                backable = true
                // Page finished loading

                if (url != null) {
                    if (url.contains("msg.php")) {
                        val response = URLDecoder.decode(url.split("?")[1], "UTF-8")

                        // Toast.makeText(this@captcha_verifyActivity, response, Toast.LENGTH_SHORT).show()
                        if (response.contains(",")) {

                            val withvalue = response.split(",")

                            Toast.makeText(this@withVerifyActivity, withvalue[0], Toast.LENGTH_SHORT).show()


                        } else {
                            Toast.makeText(this@withVerifyActivity, response, Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@withVerifyActivity, WalletActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }

        }
        // url here
        val savedata = getSharedPreferences("ChillarBuddy", MODE_PRIVATE)
        email = savedata.getString("email", "").toString()
        val dbit32 = videoplayyer.encrypt(email, Hatbc()).toString()
        val den64 = Base64.getEncoder().encodeToString(dbit32.toByteArray())
        // Load a URL
        val url = "${Constant.urllink}w.php?email=$den64"
        binding.webview2.loadUrl(url)

    }
}