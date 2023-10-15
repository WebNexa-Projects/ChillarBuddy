package com.arisu.chillarbuddy

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import com.airbnb.lottie.LottieAnimationView

class CustomProgressDialog(context: Context) {
    private val dialog: Dialog = Dialog(context, R.style.TransparentDialogTheme)

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.custom_progress_dialog, null)
        val animationView = view.findViewById<LottieAnimationView>(R.id.lottieAnimationView)
        dialog.setContentView(view)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }

    fun show(message: String = "Loading...") {

        dialog.show()
    }

    fun dismiss() {
        dialog.dismiss()
    }
}