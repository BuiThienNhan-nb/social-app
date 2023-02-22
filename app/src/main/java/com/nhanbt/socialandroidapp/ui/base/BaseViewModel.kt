package com.nhanbt.socialandroidapp.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nhanbt.socialandroidapp.utils.livedata.Event

open class BaseViewModel : ViewModel() {

    private val _loading = MutableLiveData<Event<Boolean>>()
    val loading: LiveData<Event<Boolean>> = _loading

    fun showLoading() {
        _loading.postValue(Event(true))
    }

    fun hideLoading() {
        _loading.postValue(Event(false))
    }
}
