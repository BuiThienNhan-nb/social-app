package com.nhanbt.socialandroidapp.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.nhanbt.socialandroidapp.utils.dialog.LoadingDialog

open class BaseFragment : Fragment() {

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create loading dialog
        activity?.let {
            loadingDialog = LoadingDialog(it)
        }
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
