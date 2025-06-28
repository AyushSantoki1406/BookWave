package com.example.bookwave

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.toDrawable
import com.google.firebase.database.collection.LLRBNode
import com.google.firebase.database.collection.LLRBNode.Color

object CommonUtils {

    private var progressDialog: Dialog? = null

    @RequiresApi(Build.VERSION_CODES.S)
    fun showLoading(activity: Activity): Dialog {
        progressDialog = Dialog(activity)
        progressDialog?.let {
            it.show()
            it.window?.setBackgroundDrawable(R.color.transparent.toDrawable())
            it.setContentView(R.layout.progress_dialog)
            it.setCancelable(false)
            it.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            it.setCanceledOnTouchOutside(false)
        }
        return progressDialog!!
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun applyBlurToLayout(activity: Activity, blurRadius: Float) {
        val rootLayout = activity.findViewById<View>(android.R.id.content)
        rootLayout.setRenderEffect(android.graphics.RenderEffect.createBlurEffect(blurRadius, blurRadius, android.graphics.Shader.TileMode.MIRROR))
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun removeBlurFromLayout(activity: Activity) {
        val rootLayout = activity.findViewById<View>(android.R.id.content)
        rootLayout.setRenderEffect(null)
    }

    fun dismissLoading() {
        progressDialog?.dismiss()
        progressDialog = null
    }
}

