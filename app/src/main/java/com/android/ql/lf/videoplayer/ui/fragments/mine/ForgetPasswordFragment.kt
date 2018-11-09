package com.android.ql.lf.videoplayer.ui.fragments.mine

import android.os.CountDownTimer
import android.view.View
import com.android.ql.lf.baselibaray.component.ApiParams
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.utils.*
import kotlinx.android.synthetic.main.fragment_forget_password_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject


class ForgetPasswordFragment : BaseNetWorkingFragment() {

    private val mCountDownTimer = object : CountDownTimer(VERIFY_CODE_TIME * 1000, 1 * 1000) {

        override fun onFinish() {
            mTvForgetPasswordCode.text = "重新获取？"
            mTvForgetPasswordCode.isEnabled = true
        }

        override fun onTick(millisUntilFinished: Long) {
            mTvForgetPasswordCode.isEnabled = false
            mTvForgetPasswordCode.text = "${millisUntilFinished / 1000} 秒"
        }
    }

    private var mCode: Int = -1

    override fun getLayoutId() = R.layout.fragment_forget_password_layout

    override fun initView(view: View?) {
        mTvLoginForgetPassword.setOnClickListener { finish() }
        mTvForgetPasswordCode.setOnClickListener {
            if (mEtForgetPasswordPhone.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (mEtForgetPasswordPhone.isNotPhone()) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            mCountDownTimer.start()
            mPresent.getDataByPost(0x0, getBaseParamsWithModAndAct(LOGIN_MODULE,SMS_CODE_ACT).addParam("phone", mEtForgetPasswordPhone.getTextString()))
        }
        mBtForgetPassword.setOnClickListener {
            if (mEtForgetPasswordPhone.isEmpty()) {
                toast("请输入手机号")
                return@setOnClickListener
            }
            if (mEtForgetPasswordPhone.isNotPhone()) {
                toast("请输入正确的手机号")
                return@setOnClickListener
            }
            if (mEtForgetPasswordCode.isEmpty()) {
                toast("请输入验证码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordCode.getTextString() != "$mCode") {
                toast("请输入正确的验证码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordPW.isEmpty()) {
                toast("请输入密码")
                return@setOnClickListener
            }
            if (mEtForgetPasswordPW.passwordIsNotInRange()) {
                toast("请输入6 ~ 16位密码")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x1, ApiParams().addParam(ApiParams.MOD_NAME, LOGIN_MODULE).addParam(ApiParams.ACT_NAME, FORGET_ACT).addParam("phone", mEtForgetPasswordPhone.getTextString()).addParam("password", mEtForgetPasswordPW.getTextString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在获取验证码")
        } else if (requestID == 0x1) {
            getFastProgressDialog("正在操作")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            val json = JSONObject(result as String)
            json.optInt("status").let {
                if (it == 200) {
                    toast("验证码发送成功，请注意查收")
                    mCode = json.optInt("code")
                }
            }
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    toast("密码重置成功，请登录")
                    finish()
                } else {
                    toast((check.obj as JSONObject).optString("msg"))
                }
            } else {
                toast("操作失败，请稍后重试")
            }
        }
    }

    override fun showFailMessage(requestID: Int): String {
        return if (requestID == 0x0) {
            "验证码发送失败"
        } else {
            "操作失败，请稍后重试"
        }
    }

    override fun onDestroyView() {
        mCountDownTimer.cancel()
        super.onDestroyView()
    }

}