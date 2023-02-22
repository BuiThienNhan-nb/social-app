package com.nhanbt.socialandroidapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.nhanbt.socialandroidapp.R
import com.nhanbt.socialandroidapp.data.source.remote.login.User
import com.nhanbt.socialandroidapp.databinding.LoginActivityBinding
import com.nhanbt.socialandroidapp.extensions.afterTextChanged
import com.nhanbt.socialandroidapp.ui.base.BaseActivity
import com.nhanbt.socialandroidapp.ui.home.MainActivity
import com.nhanbt.socialandroidapp.utils.Utils
import com.nhanbt.socialandroidapp.utils.dialog.DialogUtils
import com.nhanbt.socialandroidapp.utils.livedata.EventObserver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity() {
    private lateinit var binding: LoginActivityBinding

    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupViews()
    }

    private fun setupViewModel() {
        // Subscribe form state observer
        subscribeFormState()
        // Subscribe form result observer
        subscribeFormResult()
        // Subscribe loading observer
        subscribeLoadingObserver()
    }

    // Subscribe form state observer
    private fun subscribeFormState() {
        loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            binding.loginButton.isEnabled = loginState.isDataValid

            if (loginState.emailError != null) {
                binding.emailTextField.error = getString(loginState.emailError)
            } else {
                binding.emailTextField.error = null
            }

            if (loginState.passwordError != null) {
                binding.passwordTextField.error = getString(loginState.passwordError)
            } else {
                binding.passwordTextField.error = null
            }
        })
    }

    // Subscribe form result observer
    private fun subscribeFormResult() {
        loginViewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            // Proceed the result
            if (loginResult.success != null) {
                updateUiWithUser(loginResult.success)
            } else if (loginResult.error != null) {
                showLoginFailed(loginResult.error)
            } else if (loginResult.defaultError != null) {
                showLoginFailed(getString(loginResult.defaultError))
            }
        })
    }

    // Subscribe loading observer
    private fun subscribeLoadingObserver() {
        loginViewModel.loading.observe(this, EventObserver { isNeedLoading ->
            if (isNeedLoading) {
                showLoading()
            } else {
                hideLoading()
            }
        })
    }

    private fun updateUiWithUser(user: User) {
        Toast.makeText(applicationContext, getString(R.string.login_success), Toast.LENGTH_SHORT)
            .show()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun showLoginFailed(error: String) {
        DialogUtils.getMaterialDialog(this, getString(R.string.error_title), error)
        { dialogInterface, _ -> dialogInterface.cancel() }.show()
    }

    private fun setupViews() {
        val email = binding.emailTextField.editText!!
        val password = binding.passwordTextField.editText!!

        email.afterTextChanged {
            loginViewModel.loginDataChanged(
                email.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    email.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE -> {
                        // Proceed login...
                        loginViewModel.login(email.text.toString(), password.text.toString())
                    }
                }
                false
            }

            binding.loginButton.setOnClickListener {
                // Proceed login...
                loginViewModel.login(email.text.toString(), password.text.toString())
            }

            binding.forgotPasswordButton.setOnClickListener {
                // open FORGOT PASSWORD screen
                println("FORGOT_PASSWORD clicked!")
            }
        }

        binding.container.apply {
            setOnClickListener { Utils.hideSoftKeyboard(this@LoginActivity) }
        }
    }
}
