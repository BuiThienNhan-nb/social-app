package com.nhanbt.socialandroidapp.ui.login

import com.nhanbt.socialandroidapp.data.source.remote.login.User
import com.nhanbt.socialandroidapp.ui.base.IBaseResult

data class LoginResult(
    override val success: User? = null,
    override val error: String? = null,
    override val defaultError: Int? = null
) : IBaseResult<User>
