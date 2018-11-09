package com.android.ql.lf.videoplayer.ui.fragments.mine

import android.content.Context
import android.graphics.Color
import android.view.View
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseFragment
import com.android.ql.lf.videoplayer.R
import kotlinx.android.synthetic.main.fragment_selector_login_layout.*

class SelectLoginFragment : BaseFragment() {

    companion object {
        fun startSelectLoginFragment(context:Context){
            FragmentContainerActivity.from(context).setClazz(SelectLoginFragment::class.java).setHiddenToolBar(true).setNeedNetWorking(false).start()
        }
    }

    override fun getLayoutId() = R.layout.fragment_selector_login_layout

    override fun initView(view: View?) {
        (mContext as FragmentContainerActivity).setStatusBarLightColor(true)
        (mContext as FragmentContainerActivity).statusBarColor = Color.parseColor("#383a3f")
        mBtSelectorLogin.setOnClickListener {
            FragmentContainerActivity.from(mContext).setTitle("").setNeedNetWorking(true).setHiddenToolBar(true).setClazz(LoginFragment::class.java).start()
            finish()
        }

        mBtSelectorRegister.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(RegisterFragment::class.java).setTitle("").setNeedNetWorking(true).setHiddenToolBar(true).start()
            finish()
        }
    }
}