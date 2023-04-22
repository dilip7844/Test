package com.example.test

import android.app.ProgressDialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.math.roundToInt


object Common {

    @JvmStatic
    fun validateString(string: String?, message: String?): String? {
        return if (string != "null") {
            string ?: message
        } else message
    }

    val isOnline: Boolean
        get() {
            val connectivityManager =
                AppConfig.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val network = connectivityManager.activeNetwork ?: return false
                val activeNetwork = connectivityManager.getNetworkCapabilities(network)
                    ?: return false
                return when {
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            } else {
                @Suppress("DEPRECATION") val networkInfo =
                    connectivityManager.activeNetworkInfo ?: return false
                @Suppress("DEPRECATION")
                return networkInfo.isConnected
            }
        }

    @JvmStatic
    fun makeToast(msg: String?) {
        Toast.makeText(AppConfig.instance.applicationContext, msg, Toast.LENGTH_SHORT).show()
    }


    fun isMobileValid(mobile: String): Boolean {
        return mobile.length == 10
    }


    fun openFragment(activity: AppCompatActivity, fragment: DialogFragment) {
        activity.let {
            val fragmentTransaction = activity.supportFragmentManager.beginTransaction()
            if (activity.supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName) == null)
                fragment.show(fragmentTransaction, fragment.javaClass.simpleName)
            else fragmentTransaction.show(fragment).commit()
        }
    }


    fun isEmpty(textInputLayout: TextInputLayout, errMsg: String?): Boolean {
        return if (textInputLayout.editText?.text?.trim().toString() == "") {
            textInputLayout.hintTextColor =
                ColorStateList.valueOf(getColor(textInputLayout.context, R.color.red))
            textInputLayout.error = errMsg ?: "Field required"
            true
        } else {
            textInputLayout.hintTextColor =
                ColorStateList.valueOf(getColor(textInputLayout.context, R.color.gray_dark))
            textInputLayout.isErrorEnabled = false
            false
        }
    }


    fun Any?.toString(): String {
        //Log.d("Common", "toString: ")
        if (this == null) return "null"
        return toString()
    }

    fun getColor(context: Context, color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    fun getDrawable(context: Context, drawable: Int): Drawable? {
        return ContextCompat.getDrawable(context, drawable)
    }


    fun getRotateAnimation(): Animation {
        val animation: Animation = RotateAnimation(
            0.0f,
            360.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.repeatCount = -1
        animation.duration = 2000
        return animation
    }


    fun convertDpToPixel(dp: Int): Int {
        return (dp * (AppConfig.instance.resources.displayMetrics.densityDpi / 160f)).roundToInt()
    }

    /*
        fun getCircularProgressDrawable(context: Context): CircularProgressDrawable {
            val drawable = CircularProgressDrawable(context)
            drawable.setColorSchemeColors(getColor(R.color.colorSecondary))
            drawable.centerRadius = 20f
            drawable.strokeWidth = 5f
            drawable.start()
            return drawable
        }*/


    fun enableLayout(enable: Boolean, viewGroup: ViewGroup) {
        viewGroup.isEnabled = enable
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child.parent !is Spinner) {
                child.isEnabled = enable
                child.isClickable = enable
            }

            if (child is ViewGroup)
                enableLayout(enable, child)
        }
    }


    fun getWidth(): Int {
        val displayMetrics = AppConfig.instance.resources.displayMetrics
        return displayMetrics.widthPixels
    }

    fun getHeight(): Int {
        val displayMetrics = AppConfig.instance.resources.displayMetrics
        return displayMetrics.heightPixels
    }


    fun createLoadingDialog(activity: AppCompatActivity): ProgressDialog {
        var progressDialog = ProgressDialog(activity, R.style.ProgressDialogStyle)
        progressDialog.setCancelable(false)
        progressDialog.setMessage("Loading")
        progressDialog.create()
        return progressDialog
    }
}