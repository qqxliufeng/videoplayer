package com.android.ql.lf.videoplayer.ui.fragments.vip

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.user.UserInfo
import kotlinx.android.synthetic.main.fragment_recharge_vip_layout.*

class ChargeVipFragment : BaseNetWorkingFragment() {

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_recharge_vip_layout

    override fun initView(view: View?) {
        mTvRechargeVipUserNickName.text = UserInfo.user_nickname
        GlideManager.loadFaceCircleImage(mContext, UserInfo.user_pic, mIvRechargeVipUserFace)
        mRlRechargeVipWX.setOnClickListener {
            mCBRechargeVipWX.isChecked = true
            mCBRechargeVipAli.isChecked = false
        }
        mRlRechargeVipAli.setOnClickListener {
            mCBRechargeVipWX.isChecked = false
            mCBRechargeVipAli.isChecked = true
        }
        mRbRechargeVipUserOne.isChecked = true
        mTvRechargeVipMoney.text = "48"
        mCBRechargeVipWX.isChecked = true
        mRbRechargeVipUserOne.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mTvRechargeVipMoney.text = "48"
            }
        }
        mRbRechargeVipUserTwo.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mTvRechargeVipMoney.text = "144"
            }
        }
        mRbRechargeVipUserThree.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mTvRechargeVipMoney.text = "288"
            }
        }
        mRbRechargeVipUserFour.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                mTvRechargeVipMoney.text = "576"
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.recharge, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.mRecharge)
            FragmentContainerActivity.from(mContext).setTitle("充值记录").setNeedNetWorking(true).setClazz(
                RechargeRecordFragment::class.java
            ).start()
        return super.onOptionsItemSelected(item)
    }

}