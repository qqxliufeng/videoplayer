package com.android.ql.lf.videoplayer.data.user

import android.arch.lifecycle.LiveData
import com.android.ql.lf.baselibaray.utils.PreferenceUtils
import com.android.ql.lf.videoplayer.application.MyApplication
import org.json.JSONObject

const val USER_ID_FLAG = "user_id"

/**
 * 广播用户数据
 */
fun UserInfo.postUserInfo() = UserInfoLiveData.postUserInfo()

/**
 * 更新用户数据
 */
fun UserInfo.updateUserInfo(nickName:String,userPic:String){
    user_nickname = nickName
    user_pic = userPic
    this.postUserInfo()
}

/**
 * 清除用户数据
 */
fun UserInfo.clearUserInfo() {
    user_id = -1
    user_phone = null
    user_nickname = null
    user_pic = null
    user_vip = 0
    this.postUserInfo()
}

/**
 * 判断用户是否是登录状态
 */
fun UserInfo.isLogin(): Boolean = user_id != -1 && user_phone != null


/**
 * 解析用户数据
 */
fun UserInfo.jsonToUserInfo(json: JSONObject): Boolean {
    return try {
        user_id = json.optInt("user_id")
        user_nickname = json.optString("user_nickname")
        user_phone = json.optString("user_phone")
        user_pic = json.optString("user_pic")
        user_vip = json.optInt("user_vip")
        PreferenceUtils.setPrefInt(MyApplication.getInstance(), USER_ID_FLAG, UserInfo.user_id)
        true
    } catch (e: Exception) {
        false
    }
}

object UserInfo {
    var user_id: Int = -1
    var user_phone: String? = null
    var user_nickname: String? = null
    var user_pic: String? = null
    var user_vip: Int = 0
}

object UserInfoLiveData : LiveData<UserInfo>() {

    fun postUserInfo() {
        value = UserInfo
    }

}
