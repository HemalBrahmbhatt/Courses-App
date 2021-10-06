package com.brain.courses_app.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.brain.courses_app.R
import com.brain.courses_app.databinding.ActivityLoginBinding
import com.brain.courses_app.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private var _binding: ActivityLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loginViewModel.isSuccessfulMutableLiveData.observe(this, {
            if (it) {
                binding.progressBarLogin.visibility = View.GONE
                startActivity(Intent(this, MainActivity::class.java)).also { finish() }
            } else {
                binding.progressBarLogin.visibility = View.GONE
            }
        })
        binding.apply {
            btnSign.setOnClickListener {
                startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
            }
            btnLogin.setOnClickListener {
                checkAndLogin()
            }
            btnForgotPwd.setOnClickListener {
                startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkAndLogin() {
        binding.edtEmailLogin.apply {
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
        binding.edtPwdLogin.apply {
            if (text.toString().isEmpty()) {
                error = getString(R.string.pwd_error)
                requestFocus()
                return
            }
            if (text.toString().length < 6) {
                error = getString(R.string.short_pwd)
                requestFocus()
                return
            }
        }
        loginUser()
    }

    private fun loginUser() {
        binding.progressBarLogin.visibility = View.VISIBLE
        val email = binding.edtEmailLogin.text.toString().trim()
        val pwd = binding.edtPwdLogin.text.toString()
        val remember = binding.chkBoxRemember.isChecked
        loginViewModel.loginUser(email, pwd, remember)
    }
}