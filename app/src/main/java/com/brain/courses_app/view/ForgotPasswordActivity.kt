package com.brain.courses_app.view

import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.brain.courses_app.R
import com.brain.courses_app.databinding.ActivityForgotPasswordBinding
import com.brain.courses_app.viewmodel.ForgetViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    private var _binding: ActivityForgotPasswordBinding? = null
    private val binding get() = _binding!!

    private val forgetViewModel: ForgetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSentForgot.setOnClickListener {
            checkAndReset()
        }
    }

    private fun checkAndReset() {
        binding.edtEmailForgot.apply {
            if (text.toString().isEmpty()) {
                error = getString(R.string.email_error)
                requestFocus()
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(text.toString()).matches()) {
                error = getString(R.string.invalid_email)
                requestFocus()
                return
            }
        }
        sentMail(binding.edtEmailForgot.text.toString().trim())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun sentMail(email: String) {
        binding.progressBarForget.visibility = View.VISIBLE
        forgetViewModel.forgetPassword(email)
        forgetViewModel.isSuccessfulMutableLiveData.observe(this) {
            if (it) {
                binding.progressBarForget.visibility = View.GONE
                finish()
            } else {
                binding.progressBarForget.visibility = View.GONE
            }
        }
    }
}