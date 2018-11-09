package com.android.ql.lf.redpacketmonkey.interfaces

import org.json.JSONObject

/**
 * Created by lf on 18.2.8.
 * @author lf on 18.2.8
 */
interface IViewUserAction {

    fun onLogin(result: JSONObject): Boolean

    fun onLogout(): Boolean

    fun onRegister(result: JSONObject)

    fun onForgetPassword(result: JSONObject)

    fun onResetPassword(result: JSONObject)

    fun modifyInfoForName(result: String): Boolean

    fun modifyInfoForPic(result: String): Boolean

}