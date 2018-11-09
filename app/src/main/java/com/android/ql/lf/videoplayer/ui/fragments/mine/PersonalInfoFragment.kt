package com.android.ql.lf.videoplayer.ui.fragments.mine

import android.app.Activity
import android.content.Intent
import android.view.View
import com.android.ql.lf.baselibaray.data.ImageBean
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.baselibaray.utils.ImageUploadHelper
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.user.UserInfo
import com.android.ql.lf.videoplayer.data.user.updateUserInfo
import com.android.ql.lf.videoplayer.utils.*
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.fragment_personal_info_layout.*
import okhttp3.MultipartBody
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class PersonalInfoFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_personal_info_layout

    override fun initView(view: View?) {
        mTvPersonalInfoNickName.setText(UserInfo.user_nickname)
        GlideManager.loadFaceCircleImage(mContext, UserInfo.user_pic, mIvPersonalInfoFace)
        mBtPersonalInfoSave.setOnClickListener {
            if (mTvPersonalInfoNickName.isEmpty()) {
                toast("请输入用户昵称")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0, getBaseParamsWithModAndAct(USER_MODULE, PERSONAL_EDIT_ACT)
                    .addParam("nickname", mTvPersonalInfoNickName.getTextString())
                    .addParam("pic", ""))
        }
        mRlPersonalInfoFace.setOnClickListener {
            openImageChoose(MimeType.ofImage(), 1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Matisse.obtainPathResult(data).let {
                ImageUploadHelper(object : ImageUploadHelper.OnImageUploadListener {
                    override fun onActionStart() {
                        getFastProgressDialog("正在上传头像")
                    }

                    override fun onActionEnd(builder: MultipartBody.Builder?) {
                        builder?.addFormDataPart("uid","${UserInfo.user_id}")
                        mPresent.uploadFile(0x1, USER_MODULE, SEL_PIC_ACT, builder?.build()?.parts())
                    }

                    override fun onActionFailed() {
                        toast("头像上传失败……")
                    }

                }).upload(arrayListOf(ImageBean(null, it[0])))
            }
        }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在保存昵称")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        val check = checkResultCode(result)
        if (requestID == 0x0) {
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    toast("昵称保存成功")
                    UserInfo.updateUserInfo(mTvPersonalInfoNickName.getTextString(), UserInfo.user_pic
                            ?: "")
                } else {
                    toast((check.obj as JSONObject).optString("msg"))
                }
            }else{
                toast("保存失败")
            }
        } else if (requestID == 0x1) {
            if (check != null) {
                if (check.code == SUCCESS_CODE) {
                    toast("头像保存成功")
                    UserInfo.user_pic = (check.obj as JSONObject).optString("result")
                    GlideManager.loadFaceCircleImage(mContext,UserInfo.user_pic,mIvPersonalInfoFace)
                    UserInfo.updateUserInfo(UserInfo.user_nickname ?: "", UserInfo.user_pic ?: "")
                } else {
                    toast((check.obj as JSONObject).optString("msg"))
                }
            }else{
                toast("保存失败")
            }
        }
    }

}