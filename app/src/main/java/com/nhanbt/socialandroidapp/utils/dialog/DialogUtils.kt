package com.nhanbt.socialandroidapp.utils.dialog

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.nhanbt.socialandroidapp.R
import com.nhanbt.socialandroidapp.ui.login.LoginActivity

object DialogUtils {

    fun getMaterialDialog(
        context: Context,
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ): MaterialAlertDialogBuilder {
        return MaterialAlertDialogBuilder(context/*, R.style.ThemeOverlay_App_MaterialAlertDialog*/)
            .setCancelable(false)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton(
                R.string.btn_ok,
                // If message is "Your session has expired. Please log in."
                if (TextUtils.equals(message, context.getString(R.string.error_session_expired))) {
                    DialogInterface.OnClickListener { dialog, _ ->
                        run {
                            // Cancel dialog
                            dialog.cancel()
                            // Navigate to Login screen
                            val intent = Intent(context, LoginActivity::class.java)
                            context.startActivity(intent)
                            // Clear activities backstack
                            (context as AppCompatActivity).finishAffinity()
                        }
                    }
                } else {
                    listener
                })
    }
}
