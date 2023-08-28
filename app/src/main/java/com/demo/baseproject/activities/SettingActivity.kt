package com.demo.baseproject.activities

import android.content.Intent
import android.os.Bundle
import com.demo.baseproject.R
import com.demo.baseproject.databinding.ActivitySettingsBinding
import com.demo.baseproject.utils.extensions.rateApp
import com.demo.baseproject.utils.extensions.shareApp
import com.demo.baseproject.utils.extensions.showRateAppDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : BaseActivity<ActivitySettingsBinding>(ActivitySettingsBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            appBar.apply {
                title.text = getString(R.string.settings)
                backButton.setOnClickListener {
                    finish()
                }
            }
            rateApp.setOnClickListener {
                showRateAppDialog { rateApp() }
            }
            privacyPolicy.setOnClickListener {
                navigateToPrivacyPolicy()
            }
            shareApp.setOnClickListener {
                shareApp("Wow, I found a powerful Pro AI Chat app, ask any question and help you write anything you want. Start using Pro AI Chat NOW!")
            }
        }
    }

    private fun navigateToPrivacyPolicy() {
        val intent = Intent(this, PrivacyPolicyActivity::class.java)
        navigateToDifferentScreen(intent)
    }
}