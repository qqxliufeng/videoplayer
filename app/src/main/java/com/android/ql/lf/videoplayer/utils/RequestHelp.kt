package com.android.ql.lf.videoplayer.utils

import com.android.ql.lf.baselibaray.component.ApiParams
import com.android.ql.lf.videoplayer.data.user.UserInfo
import com.android.ql.lf.videoplayer.data.user.isLogin


fun getBaseParams(): ApiParams = ApiParams().addParam("pt", "android").addParam("uid", if (UserInfo.isLogin()) UserInfo.user_id else "")

fun getBaseParamsWithModAndAct(mod: String, act: String) : ApiParams = getBaseParams().addParam(ApiParams.MOD_NAME, mod).addParam(ApiParams.ACT_NAME, act)

fun getBaseParamsWithPage(mod: String, act: String, page: Int = 0, pageSize: Int = 10) : ApiParams = getBaseParamsWithModAndAct(mod, act).addParam("page", page).addParam("pagesize", pageSize)


/**        视频模块        **/
const val VIDEO_MODULE = "video"
const val VIDEO_NAV_ACT = "videoNav"
const val VIDEO_LIST_ACT = "videoList"
const val VIDEO_DETAIL_ACT = "videoDetail"
const val VIDEO_WATCHING_ACT = "videoWatching"
const val VIDEO_BELLE_ACT = "videoBelle"

/**        用户登录模块        **/

const val LOGIN_MODULE = "login"
const val LOGIN_DO_ACT = "loginDo"
const val SMS_CODE_ACT = "smscode"
const val REGISTER_DO_ACT = "registerDo"
const val FORGET_ACT = "forget"


/**      用户信息模块         **/

const val USER_MODULE = "user"
const val PERSONAL_EDIT_ACT = "personalEdit"
const val SEL_PIC_ACT = "sel_pic"
const val RESET_PASSWORD_ACT = "resetPassword"
const val LIABILITY_ACT = "liability"
const val IDEA_ACT = "idea"
const val SETTING_ACT = "setting"
const val MY_RECORDED_ACT = "myRecorded"
const val MY_BILL_ACT = "myBill"
const val DEL_RECORDED_ACT = "delRecorded"
const val DEL_BILL_ACT = "delBill"
const val RECHARGE_ACT = "Recharge"
const val RECHARGERECORD_ACT = "rechargeRecord"
