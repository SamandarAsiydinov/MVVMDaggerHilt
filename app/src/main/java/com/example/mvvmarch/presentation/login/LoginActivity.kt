package com.example.mvvmarch.presentation.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mvvmarch.R
import com.example.mvvmarch.data.login.remote.dto.LoginRequest
import com.example.mvvmarch.data.login.remote.dto.LoginResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.databinding.ActivityLoginBinding
import com.example.mvvmarch.domain.login.entity.LoginEntity
import com.example.mvvmarch.manager.SharedPrefManager
import com.example.mvvmarch.presentation.common.extensions.isEmail
import com.example.mvvmarch.presentation.common.extensions.showGenericAlertDialog
import com.example.mvvmarch.presentation.common.extensions.toast
import com.example.mvvmarch.presentation.main.MainActivity
import com.example.mvvmarch.presentation.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        login()
        goToRegisterActivity()
        observer()

    }

    private fun observer() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: LoginActivityState) {
        when (state) {
            is LoginActivityState.ShowToast -> toast(state.message)
            is LoginActivityState.IsLoading -> handleLoading(state.isLoading)
            is LoginActivityState.Init -> Unit
            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.rawResponse)
            is LoginActivityState.SuccessLogin -> handleSuccessLogin(state.loginEntity)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.loginButton.isEnabled = !isLoading
        binding.registerButton.isEnabled = !isLoading
        binding.loadingProgressBar.isIndeterminate = isLoading
        if (!isLoading) {
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun handleSuccessLogin(loginEntity: LoginEntity) {
        sharedPref.saveToken(loginEntity.token)
        goToMainActivity()
    }

    private fun handleErrorLogin(rawResponse: WrapperResponse<LoginResponse>) {
        showGenericAlertDialog(rawResponse.message)
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun goToRegisterActivity() {
        binding.registerButton.setOnClickListener {
            openRegisterActivity.launch(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun login() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (validate(email, password)) {
                viewModel.login(LoginRequest(email, password))
            }
        }
    }

    private fun validate(email: String, password: String): Boolean {
        resetAllInputError()
        if (!email.isEmail()) {
            setEmailError(getString(R.string.error_email_not_valid))
            return false
        }

        if (password.length < 8) {
            setPasswordError(getString(R.string.error_password_not_valid))
            return false
        }

        return true
    }

    private fun resetAllInputError() {
        setEmailError(null)
        setPasswordError(null)
    }

    private fun setEmailError(e: String?) {
        binding.emailInput.error = e
    }

    private fun setPasswordError(e: String?) {
        binding.passwordInput.error = e
    }
    private val openRegisterActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            goToMainActivity()
        }
    }
}