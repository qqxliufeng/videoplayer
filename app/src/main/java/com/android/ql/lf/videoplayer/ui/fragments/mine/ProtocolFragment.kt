package com.android.ql.lf.videoplayer.ui.fragments.mine

import android.graphics.Bitmap
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.android.ql.lf.baselibaray.ui.fragment.BaseFragment
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.utils.LIABILITY_ACT
import com.android.ql.lf.videoplayer.utils.USER_MODULE
import com.android.ql.lf.videoplayer.utils.getBaseParamsWithModAndAct
import kotlinx.android.synthetic.main.fragment_protocol_layout.*

class ProtocolFragment : BaseNetWorkingFragment() {

    override fun getLayoutId() = R.layout.fragment_protocol_layout

    override fun initView(view: View?) {
        mWbProtocol.settings.javaScriptEnabled = true
        mWbProtocol.settings.domStorageEnabled = true
        mWbProtocol.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }
        }
        mWbProtocol.webChromeClient = object : WebChromeClient(){}
        mTvProtocol.setOnClickListener {
            finish()
        }
        mPresent.getDataByPost(0x0, getBaseParamsWithModAndAct(USER_MODULE, LIABILITY_ACT))
    }


    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在加载……")
    }


    override fun onHandleSuccess(requestID: Int, obj: Any?) {
        super.onHandleSuccess(requestID, obj)
        obj?.let {
            if (it is String) {
                mWbProtocol.loadData(it, "text/html;charset=utf-8", null)
            }
        }
    }

    override fun showFailMessage(requestID: Int): String {
        return "加载失败"
    }

}