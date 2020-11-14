package com.anos.covid19.utils

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import java.lang.Exception

class DialogUtil(private val context: Activity) {

    enum class Action {
        NEGATIVE, NEUTRAL, POSITIVE
    }

    enum class Style {
        SINGLE_OK, YES_NO, OK_CANCEL
    }

    interface IDialogListener {
        fun onClicked(action: Action)
    }

    companion object {
        private var instance: DialogUtil? = null

        fun get(context: Activity): DialogUtil {
            return instance ?: DialogUtil(context).also { 
                instance = it
            }
        }

        fun releaseInstance() {
            instance = null
        }
    }

    private var dialogBuilder: AlertDialog.Builder? = null

    fun showDialog(title: String?, message: String, style: Style, listener: IDialogListener?) {
        if (context.isFinishing)
            return
        if (dialogBuilder == null) {
            dialogBuilder = AlertDialog.Builder(context)
        }
        if (!title.isNullOrEmpty()) {
            dialogBuilder?.setTitle(title)
        }
        dialogBuilder?.setMessage(message)
        when (style) {
            Style.SINGLE_OK -> {
                dialogBuilder?.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    listener?.onClicked(Action.POSITIVE)
                }
            }
            Style.YES_NO -> {
                dialogBuilder?.setPositiveButton("YES") { dialog, _ ->
                    dialog.dismiss()
                    listener?.onClicked(Action.POSITIVE)
                }
                dialogBuilder?.setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                    listener?.onClicked(Action.NEGATIVE)
                }
            }
            Style.OK_CANCEL -> {
                dialogBuilder?.setPositiveButton("OK") { dialog, _ ->
                    dialog.dismiss()
                    listener?.onClicked(Action.POSITIVE)
                }
                dialogBuilder?.setNegativeButton("CANCEL") { dialog, _ ->
                    dialog.dismiss()
                    listener?.onClicked(Action.NEGATIVE)
                }
            }
        }
        dialogBuilder?.setCancelable(false)
        try {
            dialogBuilder?.create()?.show()
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}