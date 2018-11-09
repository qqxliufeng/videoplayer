package com.android.ql.lf.videoplayer.ui.fragments.other

import android.view.View
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseFragment
import com.android.ql.lf.baselibaray.utils.CacheDataManager
import com.android.ql.lf.baselibaray.utils.VersionHelp
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.ui.fragments.mine.ResetPasswordFragment
import kotlinx.android.synthetic.main.fragment_setting_layout.*
import org.jetbrains.anko.support.v4.toast

class SettingFragment : BaseFragment() {


    override fun getLayoutId() = R.layout.fragment_setting_layout

    override fun initView(view: View?) {
        mTvSettingCacheSize.text = CacheDataManager.getTotalCacheSize(mContext)
        mTvSettingVersionName.text = "V${VersionHelp.currentVersionName(mContext)}"
        mTvSettingCacheSize.setOnClickListener {
            toast("缓存清除成功")
            CacheDataManager.clearAllCache(mContext)
            mTvSettingCacheSize.text = CacheDataManager.getTotalCacheSize(mContext)
        }
        mTvSettingResetPassword.setOnClickListener {
            FragmentContainerActivity.from(mContext).setClazz(ResetPasswordFragment::class.java).setTitle("重置密码").setNeedNetWorking(true).start()
        }
    }
}