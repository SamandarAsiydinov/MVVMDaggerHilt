package com.example.mvvmarch.presentation.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.example.mvvmarch.R
import com.example.mvvmarch.data.register.remote.dto.RegisterRequest
import com.example.mvvmarch.data.register.remote.dto.RegisterResponse
import com.example.mvvmarch.data.utils.WrapperResponse
import com.example.mvvmarch.databinding.ActivityRegisterBinding
import com.example.mvvmarch.domain.register.entity.RegisterEntity
import com.example.mvvmarch.manager.SharedPrefManager
import com.example.mvvmarch.presentation.common.extensions.isEmail
import com.example.mvvmarch.presentation.common.extensions.showGenericAlertDialog
import com.example.mvvmarch.presentation.common.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    @Inject
    lateinit var sharedPref: SharedPrefManager

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUp()
        observe()

        binding.backButton.setOnClickListener { finish() }

    }

    private fun observe() {
        viewModel.state.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach {
                handleState(it)
            }
            .launchIn(lifecycleScope)
    }

    private fun handleState(state: RegisterActivityState) {
        when(state) {
            is RegisterActivityState.IsLoading -> handleLoading(state.isLoading)
            is RegisterActivityState.Init -> Unit
            is RegisterActivityState.ShowToast -> toast(state.message)
            is RegisterActivityState.SuccessRegister -> handleSuccessRegister(state.registerEntity)
            is RegisterActivityState.ErrorRegister -> handleErrorRegister(state.rawResponse)
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.loadingProgressBar.isIndeterminate = isLoading
        binding.registerButton.isEnabled = !isLoading
        binding.backButton.isEnabled = !isLoading
        if (!isLoading) {
            binding.loadingProgressBar.progress = 0
        }
    }

    private fun handleErrorRegister(rawResponse: WrapperResponse<RegisterResponse>) {
        showGenericAlertDialog(rawResponse.message)
    }

    private fun handleSuccessRegister(entity: RegisterEntity) {
        sharedPref.saveToken(entity.token)
        setResult(RESULT_OK)
        finish()
    }

    private fun signUp() {
        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validates(name, email, password)) {
                viewModel.register(RegisterRequest(name, email, password))
            }
        }
    }

    private fun validates(name: String, email: String, password: String): Boolean {
        resetAllErrors()

        if (name.isEmpty()) {
            setNameError(getString(R.string.error_name_not_valid))
            return false
        }
        if (!email.isEmail()) {
            setEmailError(getString(R.string.error_email_not_valid))
            return false
        }
        if (password.length < 8) {
            setPasswordError(getString(R.string.error_password_not_valid))
        }
        return true
    }

    private fun setNameError(s: String?) {
        binding.nameInput.error = s
    }

    private fun setEmailError(s: String?) {
        binding.emailInput.error = s
    }

    private fun setPasswordError(s: String?) {
        binding.passwordInput.error = s
    }

    private fun resetAllErrors() {
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
    }
}