package com.example.mycustombrowser

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mycustombrowser.databinding.ActivityMainBinding
import androidx.core.content.edit
import androidx.core.graphics.toColorInt

class BrowserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val prefs = getSharedPreferences("browser_prefs", MODE_PRIVATE)
        val lastUrl = prefs.getString("last_url", "")


        if (lastUrl.isNullOrEmpty()) {
            binding.webView.loadUrl("https://www.google.com")
        } else {
            binding.webView.loadUrl(lastUrl)
            binding.textInputEditText.setText(lastUrl)
        }

        binding.webView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String?) {
                    binding.toolbar.title = title ?: getString(R.string.app_name)
                }
            }
        }

        binding.searchButton.setOnClickListener {
            val url = binding.textInputEditText.text.toString().trim()

            if (url.isEmpty()) {
                Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.webView.loadUrl(url)
            prefs.edit {
                putString("last_url", url)
            }

            Toast.makeText(this, "Loading: $url", Toast.LENGTH_SHORT).show()
        }

        binding.buttonBack.setOnClickListener {
            if (binding.webView.canGoBack()) {
                binding.webView.goBack()
            } else {
                Toast.makeText(this, "Nothing to go back to", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonReset.setOnClickListener {
            binding.textInputEditText.setText("")
            Toast.makeText(this, "Field cleared", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_portrait, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.options_green -> setSearchButtonColor(COLOR_GREEN.toColorInt())
            R.id.options_blue -> setSearchButtonColor(COLOR_BLUE.toColorInt())
            R.id.options_violet -> setSearchButtonColor(COLOR_VIOLET.toColorInt())
            R.id.options_brown -> setSearchButtonColor(COLOR_BROWN.toColorInt())
            R.id.options_yellow -> setSearchButtonColor(COLOR_YELLOW.toColorInt())
            R.id.options_orange -> setSearchButtonColor(COLOR_ORANGE.toColorInt())
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setSearchButtonColor(color: Int) {
        binding.searchButton.backgroundTintList = ColorStateList.valueOf(color)
    }
}
