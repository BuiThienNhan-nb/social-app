package com.nhanbt.socialandroidapp.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.nhanbt.socialandroidapp.R
import com.nhanbt.socialandroidapp.data.Result
import com.nhanbt.socialandroidapp.data.source.repositories.ILoginRepository
import com.nhanbt.socialandroidapp.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: ILoginRepository
) : BaseViewModel() {
    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun login(username: String, password: String) {
        showLoading()
        viewModelScope.launch {
            when (val result = loginRepository.login(username, password)) {
                is Result.Success -> {
                    hideLoading()
                    _loginResult.value = LoginResult(success = result.data.user)
                }

                is Result.Error -> {
                    hideLoading()
                    val errorMessage = result.message
                    _loginResult.value = LoginResult(error = errorMessage)
                }
                else -> {
                    hideLoading()
                    _loginResult.value = LoginResult(defaultError = R.string.default_error)
                }
            }
        }
    }

    fun loginDataChanged(email: String, password: String) {
        if (isEmailEmpty(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.email_required)
            return
        }
        if (!isEmailValid(email)) {
            _loginForm.value = LoginFormState(emailError = R.string.email_invalid)
            return
        }
        if (isPasswordEmpty(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.password_required)
            return
        }
        if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.password_invalid)
            return
        }
        _loginForm.value = LoginFormState(isDataValid = true)
        return
    }

    // A placeholder username validation check
    private fun isEmailValid(username: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    private fun isEmailEmpty(username: String): Boolean {
        return username.isEmpty()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        val specialCharacters = "-@%\\[\\}+'!/#$^?:;,\\(\"\\)~`.*=&\\{>\\]<_"
        val pattern =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[$specialCharacters])(?=\\S+$).{6,}$"
        return (Pattern.compile(pattern).matcher(password).matches())
    }

    private fun isPasswordEmpty(password: String): Boolean {
        return password.isEmpty()
    }

}
