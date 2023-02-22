package com.nhanbt.socialandroidapp.utils.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.nhanbt.socialandroidapp.R

class LoadingDialog(context: Context) {

    private var dialog: Dialog = Dialog(context)

    init {
        dialog.setContentView(R.layout.layout_dialog_loading)
        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        dialog.setCancelable(false)
    }

    fun show() {
        dialog.show()
    }

    fun hide() {
        dialog.cancel()
    }

    fun isShowing(): Boolean {
        return dialog.isShowing
    }
}
