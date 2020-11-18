package com.meuus90.booksearcher.base.view.ext

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.LinearLayout

fun Window.setSystemBarTextWhite() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = findViewById<View>(android.R.id.content)
        val flags = view.systemUiVisibility
        view.systemUiVisibility = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}

fun Window.setSystemBarTextDark() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val view = findViewById<View>(android.R.id.content)
        val flags = view.systemUiVisibility
        view.systemUiVisibility = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
}

fun Dialog.setDefaultWindowTheme() {
    window?.apply {
        setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        statusBarColor = Color.TRANSPARENT

        setSystemBarTextDark()
        setDimAmount(0.3f)
    }
}

fun View.show(): View {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
    return this
}

/**
 * Hide the view. (visibility = View.INVISIBLE)
 */
fun View.hide(): View {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
    return this
}

/**
 * Remove the view (visibility = View.GONE)
 */
fun View.gone(): View {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
    return this
}