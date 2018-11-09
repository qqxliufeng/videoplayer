package com.android.ql.lf.videoplayer.utils

import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.Html
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.user.UserInfo
import com.android.ql.lf.videoplayer.data.user.isLogin
import com.android.ql.lf.videoplayer.ui.fragments.mine.SelectLoginFragment
import java.util.regex.Pattern

val PHONE_REG = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}\$"
val IDCARD_REG = "^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)\$"
val MONEY_REG = "^(([1-9]\\d*)|([0]))(\\.(\\d){0,2})?\$"

/**
 * 显示SnackBar
 */
fun View.showSnackBar(message: String) {
    val snackBar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    snackBar.view.setBackgroundResource(R.color.colorPrimary)
    snackBar.show()
}

/**
 * 校验输入框是否为空
 */
fun EditText.isEmpty(): Boolean {
    return TextUtils.isEmpty(this.text.toString().trim())
}

fun EditText.isNotMoney(): Boolean {
    return !Pattern.compile(MONEY_REG).matcher(this.text).matches()
}

fun EditText.isNotPhone(): Boolean {
    return !Pattern.compile(PHONE_REG).matcher(this.text).matches()
}

fun EditText.passwordIsNotInRange(startIndex: Int = 6, endInt: Int = 16):Boolean {
    return getTextString().length > endInt || getTextString().length < startIndex
}


fun String.isPhone(): Boolean {
    return Pattern.compile(PHONE_REG).matcher(this).matches()
}

fun EditText.isIdCard(): Boolean {
    return Pattern.compile(IDCARD_REG).matcher(this.text).matches()
}

fun String.isIdCard(): Boolean {
    return Pattern.compile(IDCARD_REG).matcher(this).matches()
}

fun EditText.getTextString(): String {
    return this.text.toString().trim()
}

fun EditText.setFirstPoint() {
    if (this.text.startsWith(".")) {
        setText("0.")
        this.setSelection(this.text.toString().length)
    }
}


fun EditText.setTextChangedListener(afterTextWatcher: (Editable?) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextWatcher(s)
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}


fun EditText.getFormateFloat(): String? {
    try {
        if (this.isEmpty()) {
            return null
        }
        return java.lang.Float.parseFloat(getTextString()).toString()
    } catch (e: Exception) {
        return null
    }
}

fun TextView.setDiffColorText(source1: String, source2: String, color1: String = "#c8c9ca", color2: String = "#545557") {
    text = Html.fromHtml(source1.fromHtml(color1) + source2.fromHtml(color2))
}


fun String.fromHtml(color: String = "#c8c9ca"): String {
    return "<font color='$color'>$this</font>"
}

fun View.doClickWithUserStatusStart(token: String, action: (view: View) -> Unit) {
    setOnClickListener {
        if (UserInfo.isLogin()) {
            action(this)
        } else {
//            UserInfo.loginToken = token
            SelectLoginFragment.startSelectLoginFragment(this.context)
        }
    }
}

//fun View.doClickWithUseStatusEnd() {
//    performClick()
//    UserInfo.resetLoginSuccessDoActionToken()
//}

