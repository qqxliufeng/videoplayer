package com.android.ql.lf.videoplayer.ui.fragments.other

import android.content.Context
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseFragment
import com.android.ql.lf.videoplayer.R
import kotlinx.android.synthetic.main.fragment_web_view_layout.*
import org.jetbrains.anko.bundleOf

/**
 * Created by lf on 18.11.10.
 * @author lf on 18.11.10
 */
class WebViewFragment : BaseFragment() {

    companion object {
        fun startWebViewFragment(context: Context, url: String) {
            FragmentContainerActivity.from(context).setExtraBundle(bundleOf(Pair("url",url))).setTitle("").setClazz(WebViewFragment::class.java).start()
        }
    }

    override fun getLayoutId() = R.layout.fragment_web_view_layout

    override fun initView(view: View?) {
        mWVContainer.settings.javaScriptEnabled = true
        mWVContainer.settings.domStorageEnabled = true
        mWVContainer.settings.allowFileAccess = true
        mWVContainer.webChromeClient = object : WebChromeClient() {}
        mWVContainer.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
        mWVContainer.loadUrl(arguments?.getString("url", "") ?: "")
    }
}