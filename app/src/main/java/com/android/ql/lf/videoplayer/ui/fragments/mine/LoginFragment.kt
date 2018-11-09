package com.android.ql.lf.videoplayer.ui.fragments.mine

import android.view.View
import com.android.ql.lf.baselibaray.component.ApiParams
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.user.UserInfo
import com.android.ql.lf.videoplayer.data.user.jsonToUserInfo
import com.android.ql.lf.videoplayer.data.user.postUserInfo
import com.android.ql.lf.videoplayer.utils.*
import kotlinx.android.synthetic.main.fragment_login_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class LoginFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_login_layout

    override fun initView(view: View?) {
        mTvLoginRegister.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(RegisterFragment::class.java).setTitle("").setNeedNetWorking(true).setHiddenToolBar(true).start()
        }

        mTvLoginForgetPassword.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(ForgetPasswordFragment::class.java).setTitle("").setNeedNetWorking(true).setHiddenToolBar(true).start()
        }

        mBtLogin.setOnClickListener {
            if (mEtLoginPhone.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (mEtLoginPhone.isNotPhone()) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtLoginPassword.isEmpty()) {
                toast("请输入密码")
                return@setOnClickListener
            }
            if (mEtLoginPassword.passwordIsNotInRange()) {
                toast("请输入6 ~ 16位密码")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0, getBaseParamsWithModAndAct(LOGIN_MODULE,LOGIN_DO_ACT).addParam("phone",mEtLoginPhone.getTextString()).addParam("password",mEtLoginPassword.getTextString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在登录……")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        val check = checkResultCode(result)
        if (check!=null){
            if (check.code == SUCCESS_CODE){
                onHandleSuccess(requestID,(check.obj as JSONObject).optJSONObject(RESULT_OBJECT))
            }else{
                toast((check.obj as JSONObject).optString("msg"))
            }
        }
    }

    override fun onHandleSuccess(requestID: Int, obj: Any?) {
        super.onHandleSuccess(requestID, obj)
        if (obj != null && obj is JSONObject){
            if (UserInfo.jsonToUserInfo(json = obj)) {
                UserInfo.postUserInfo()
                finish()
            }else{
                toast("登录失败，请稍后重试……")
            }
        }
    }

    override fun showFailMessage(requestID: Int): String {
        return "登录失败，请稍后重试……"
    }

}