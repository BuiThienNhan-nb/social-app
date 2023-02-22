package com.nhanbt.socialandroidapp.utils

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.inputmethod.InputMethodManager
import com.google.gson.Gson

object Utils {

    @JvmStatic
    fun hideSoftKeyboard(activity: Activity) {
        val inputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, 0)
    }

    /**
     * Convert json to model
     * @param jsonString input json
     * @param modelClass type of model to convert
     */
    @JvmStatic
    inline fun <reified T : Any> fromJson(jsonString: String, modelClass: Class<T>): T? {
        val gson = Gson()
        return gson.fromJson(jsonString, modelClass)
    }

    /**
     * Check network connectivity status
     * @return true if network is available, otherwise return false.
     */
    @JvmStatic
    fun isNetworkAvailable(context: Context): Boolean {
        var isNetworkAvailable = false
        val connectivityManager =
            context.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                isNetworkAvailable = true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                isNetworkAvailable = true
            }
        }
        return isNetworkAvailable
    }

}
