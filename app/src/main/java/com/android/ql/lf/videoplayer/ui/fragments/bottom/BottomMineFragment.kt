package com.android.ql.lf.videoplayer.ui.fragments.bottom

import android.arch.lifecycle.Observer
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.user.UserInfo
import com.android.ql.lf.videoplayer.data.user.UserInfoLiveData
import com.android.ql.lf.videoplayer.data.user.isLogin
import com.android.ql.lf.videoplayer.ui.fragments.mine.*
import com.android.ql.lf.videoplayer.ui.fragments.other.SettingFragment
import com.android.ql.lf.videoplayer.utils.doClickWithUserStatusStart
import kotlinx.android.synthetic.main.fragment_bottom_mine_layout.*

class BottomMineFragment : BaseFragment() {

    override fun getLayoutId() = R.layout.fragment_bottom_mine_layout

    override fun initView(view: View?) {

        if (UserInfo.isLogin()) {
            GlideManager.loadFaceCircleImage(mContext, UserInfo.user_pic, mIvBottomMineUserFace)
            mTvBottomMineUserNickName.text = UserInfo.user_nickname
        } else {
            mIvBottomMineUserFace.setImageResource(R.drawable.img_glide_circle_load_default)
            mTvBottomMineUserNickName.text = "登录/注册"
        }

        (mTvMineTitle.layoutParams as ViewGroup.MarginLayoutParams).topMargin = statusBarHeight

        mLlBottomMineFaceContainer.doClickWithUserStatusStart("") {
            FragmentContainerActivity.from(mContext).setClazz(PersonalInfoFragment::class.java).setNeedNetWorking(true).setTitle("账号资料").start()
        }

        mLlMinePersonalInfo.doClickWithUserStatusStart(""){
            FragmentContainerActivity.from(mContext).setClazz(PersonalInfoFragment::class.java).setNeedNetWorking(true).setTitle("账号资料").start()
        }

        mTvMineGkjl.doClickWithUserStatusStart("") {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("观看记录").setClazz(RecordFragment::class.java).start()
        }

        mTvMineWdkd.doClickWithUserStatusStart("") {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("我的看单").setClazz(BillFragment::class.java).start()
        }

        mTvMineZxkf.doClickWithUserStatusStart("")  {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("客服反馈").setClazz(CustomServiceFragment::class.java).start()
        }

        mTvMineMzsm.doClickWithUserStatusStart("")  {
            FragmentContainerActivity.from(mContext).setTitle("免责声明").setNeedNetWorking(true).setClazz(ProtocolFragment::class.java).start()
        }

        mTvMineSetting.doClickWithUserStatusStart("")  {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("设置").setClazz(SettingFragment::class.java).start()
        }

        UserInfoLiveData.observe(this, Observer<UserInfo> {
            GlideManager.loadFaceCircleImage(mContext, UserInfo.user_pic, mIvBottomMineUserFace)
            mTvBottomMineUserNickName.text = UserInfo.user_nickname
        })
    }
}