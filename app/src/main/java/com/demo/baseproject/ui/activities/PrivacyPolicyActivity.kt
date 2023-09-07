package com.demo.baseproject.ui.activities

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.demo.baseproject.R
import com.demo.baseproject.databinding.ActivityPrivacyPolicyBinding
import com.demo.baseproject.network.NetworkConstants
import com.demo.baseproject.ui.base.BaseActivity
import com.demo.baseproject.utils.extensions.gone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyActivity :
    BaseActivity<ActivityPrivacyPolicyBinding>(ActivityPrivacyPolicyBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            appBar.apply {
                title.text = getString(R.string.privacy_policy)
                backButton.setOnClickListener { finish() }
            }
            webview.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.gone()
                }
            }
            webview.loadUrl(NetworkConstants.PRIVACY_POLICY)
            webview.settings.javaScriptEnabled = true
        }
    }
}
