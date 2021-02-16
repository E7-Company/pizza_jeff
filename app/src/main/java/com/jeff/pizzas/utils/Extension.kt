package com.jeff.pizzas.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.jeff.pizzas.model.UserStatus
import com.jeff.pizzas.ui.register.RegisterActivity
import com.jeff.pizzas.utils.Constants.USERSTATUS
import java.text.SimpleDateFormat
import java.util.*

fun SharedPreferences.putUserStatus(userStatus: UserStatus?) {
    edit().putString(USERSTATUS, userStatus?.name).apply()
}

fun SharedPreferences.removeUserStatus(){
    edit().remove(USERSTATUS).apply()
}

fun ImageView.loadCircularImage(url: String, view: View) {
    Glide.with(view)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .thumbnail(0.5f)
        .transform(CircleCrop())
        .into(this)
}

fun Double.format(decimals: Int = 2): String {
    return "%.${decimals}f€".format(this)
}

fun String.isEmail(): Boolean {
    val format = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)\$".toRegex()
    return matches(format)
}

fun Long.timestampToDate(): String {
    val sdf = SimpleDateFormat("MM/dd/yyyy")
    val date = Date(this)
    return sdf.format(date)
}

fun Any.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this.toString(), duration).apply { show() }
}

fun Activity.triggerRestart() {
    val intent = Intent(this, RegisterActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.finish()
    this.startActivity(intent)
    Runtime.getRuntime().exit(0)
}