package com.android.ql.lf.videoplayer.ui.fragments.mine

import android.content.ClipboardManager
import android.content.Context
import android.view.View
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.utils.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_custom_service_layout.*
import org.jetbrains.anko.support.v4.toast

class CustomServiceFragment : BaseNetWorkingFragment() {

    var settingBean: SettingBean? = null

    override fun getLayoutId() = R.layout.fragment_custom_service_layout

    override fun initView(view: View?) {
        mTvCustomServiceCopyQQ.setOnClickListener {
            if (settingBean != null) {
                val copyBoard = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                copyBoard.text = settingBean?.clientserviceqq
                toast("QQ复制成功")
            }
        }
        mBtCustomService.setOnClickListener {
            if (mEtCustomServiceFeedBack.isEmpty()) {
                toast("请输入反馈内容")
                return@setOnClickListener
            }
            mPresent.getDataByPost(0x0, getBaseParamsWithModAndAct(USER_MODULE, IDEA_ACT)
                    .addParam("content", mEtCustomServiceFeedBack.getTextString())
                    .addParam("wx", mEtCustomServiceWX.getTextString())
                    .addParam("qq", mEtCustomServiceQQ.getTextString()))
        }
        mPresent.getDataByPost(0x1, getBaseParamsWithModAndAct(USER_MODULE, SETTING_ACT))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x0) {
            getFastProgressDialog("正在上传……")
        } else if (requestID == 0x1) {
            getFastProgressDialog("加载中……")
        }
    }

    override fun onHandleSuccess(requestID: Int, obj: Any?) {
        super.onHandleSuccess(requestID, obj)
        if (requestID == 0x1) {
            obj?.apply {
                settingBean = Gson().fromJson(this.toString())
                mTvCustomServiceQQ.text = "客服QQ：${settingBean?.clientserviceqq}"
                val dialog = CustomServiceDialogFragment()
                val content = """Hi~帅哥，有什么不满意，直接来找我哦~我的客服工作时间： ${settingBean?.clientservicetime} 加我QQ： ${settingBean?.clientserviceqq} 如果充值后没有成功成为VIP,请立即联系我们进行查询，如果有疑问请优先联系我哦~"""
                dialog.setData(content)
                dialog.show(childFragmentManager, "custom_service_dialog")
            }
        }else{
            toast("上传成功")
            finish()
        }
    }
}

class SettingBean {
    var logos: String? = null
    var versions: String? = null
    var iosdown: String? = null
    var androiddown: String? = null
    var clientservicetime: String? = null
    var clientserviceqq: String? = null
    var sms_title: String? = null
    var sms_id: String? = null
    var sms_name: String? = null
    var sms_pass: String? = null
    var title: String? = null
    var titlename: String? = null
    var titlenickname: String? = null
    var phone: String? = null
    var ip: String? = null
    var max: String? = null
    var protocol: String? = null
    var logo: String? = null
    var code: String? = null
}