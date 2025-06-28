package com.example.bookwave

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable

object CommonUtils2 {

    private var progressDialog: Dialog? = null

    @RequiresApi(Build.VERSION_CODES.S)
    fun showLoading(context: Context): Dialog {
        progressDialog = Dialog(context)

        progressDialog?.let {
            it.show()
            it.window?.setBackgroundDrawable(R.color.transparent.toDrawable())
            it.window?.setBackgroundBlurRadius(100)
            it.setContentView(R.layout.loading_dialog)
            it.setCancelable(false)
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.setCanceledOnTouchOutside(false)
        }

        return progressDialog!!
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun applyBlurToLayout(layout: View, blurRadius: Float) {
        layout.apply {
            setRenderEffect(android.graphics.RenderEffect.createBlurEffect(blurRadius, blurRadius, android.graphics.Shader.TileMode.MIRROR))
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun removeBlurFromLayout(layout: View) {
        layout.apply {
            setRenderEffect(null)
        }
    }

    fun dismissLoading() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}
