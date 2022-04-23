package com.brain.courses_app.view

import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.brain.courses_app.R
import com.brain.courses_app.databinding.ActivitySignUpBinding
import com.brain.courses_app.model.User
import com.brain.courses_app.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private var _binding: ActivitySignUpBinding? = null
    private val binding get() = _binding!!
    private val registerViewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerViewModel.isRegisteredMutableLiveData.observe(this) {
            if (it) {
                binding.progressBarSign.visibility = View.GONE
                Toast.makeText(
                    this@SignUpActivity,
                    getString(R.string.register_success),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                binding.progressBarSign.visibility = View.GONE
            }
        }
        binding.apply {
            btnSignup.setOnClickListener {
                checkAndRegister()
            }
            btnLoginSign.setOnClickListener {
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun checkAndRegister() {
        binding.apply {
            edtNameSign.apply {
                if (text.toString().isEmpty()) {
                    error = getString(R.string.name_error)
                    requestFocus()
                    return
                }
            }
            edtEmailSign.apply {
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
            edtAgeSign.apply {
                if (text.toString().isEmpty()) {
                    error = getString(R.string.age_error)
                    requestFocus()
                    return
                }
            }
            if (edtConfirmPwdSign.text.toString().isEmpty()) {
                edtConfirmPwdSign.error = getString(R.string.confirm_pwd_error)
                edtConfirmPwdSign.requestFocus()
                return
            }
            edtPwdSign.apply {
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
                if (text.toString() != edtConfirmPwdSign.text.toString()) {
                    error = getString(R.string.confirm_error)
                    edtConfirmPwdSign.error = getString(R.string.confirm_error)
                    edtConfirmPwdSign.requestFocus()
                    return
                }
            }
            registerUser()
        }
    }

    private fun registerUser() {
        binding.apply {
            progressBarSign.visibility = View.VISIBLE
            val user = User(
                name = edtNameSign.text.toString(),
                age = edtAgeSign.text.toString().toInt(),
                email = edtEmailSign.text.toString().trim(),
                password = edtPwdSign.text.toString()
            )
            registerViewModel.registerUser(user)
        }
    }
}