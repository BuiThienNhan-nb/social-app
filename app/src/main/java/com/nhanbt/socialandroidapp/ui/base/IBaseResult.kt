package com.nhanbt.socialandroidapp.ui.base

interface IBaseResult<out T> {
    val success: T?
    val error: String?
    val defaultError: Int?
}
