package com.nhanbt.socialandroidapp.ui.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nhanbt.socialandroidapp.utils.dialog.LoadingDialog

open class BaseActivity : AppCompatActivity() {

    private lateinit var loadingDialog: LoadingDialog

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create loading dialog
        loadingDialog = LoadingDialog(this)
    }

    fun showLoading() {
        if (loadingDialog.isShowing()) {
            loadingDialog.hide()
        }
        loadingDialog.show()
    }

    fun hideLoading() {
        if (loadingDialog.isShowing()) {
            loadingDialog.hide()
        }
    }
}
