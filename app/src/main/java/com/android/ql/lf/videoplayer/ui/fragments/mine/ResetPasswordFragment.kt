package com.android.ql.lf.videoplayer.ui.fragments.mine

import android.view.View
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.utils.*
import kotlinx.android.synthetic.main.fragment_reset_password_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class ResetPasswordFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_reset_password_layout


    override fun initView(view: View?) {
        mBtResetPassword.setOnClickListener {
            if (mEtResetPasswordOld.isEmpty()) {
                toast("请输入原始密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordOld.passwordIsNotInRange()) {
                toast("请输入6 ~ 16位密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordNew.isEmpty()) {
                toast("请输入新密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordNew.passwordIsNotInRange()) {
                toast("请输入6 ~ 16位密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordNewConfirm.isEmpty()) {
                toast("请再次输入新密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordNewConfirm.passwordIsNotInRange()) {
                toast("请输入6 ~ 16位密码")
                return@setOnClickListener
            }
            if (mEtResetPasswordNew.getTextString() != mEtResetPasswordNewConfirm.getTextString()) {
                toast("两次密码不一致")
                mEtResetPasswordNew.setText("")
                mEtResetPasswordNewConfirm.setText("")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0, getBaseParamsWithModAndAct(USER_MODULE, RESET_PASSWORD_ACT).addParam("password", mEtResetPasswordOld.getTextString()).addParam("repassword", mEtResetPasswordNew.getTextString()))
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在修改密码")
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        checkResultCode(result)?.let {
            toast((it.obj as JSONObject).optString("msg"))
            if (it.code == SUCCESS_CODE){
                finish()
            }
        }
    }

    override fun showFailMessage(requestID: Int): String {
        return "密码修改失败"
    }
}