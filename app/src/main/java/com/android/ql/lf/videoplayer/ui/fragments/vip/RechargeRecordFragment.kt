package com.android.ql.lf.videoplayer.ui.fragments.vip

import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.user.RechargeBean
import com.android.ql.lf.videoplayer.utils.RECHARGERECORD_ACT
import com.android.ql.lf.videoplayer.utils.USER_MODULE
import com.android.ql.lf.videoplayer.utils.getBaseParamsWithPage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.backgroundColor

class RechargeRecordFragment : BaseRecyclerViewFragment<RechargeBean>() {

    override fun createAdapter() =
        object : BaseQuickAdapter<RechargeBean, BaseViewHolder>(R.layout.fragment_recharge_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: RechargeBean?) {
                helper?.setText(R.id.mTvRechargeItemName, item?.recharge_record_name)
                helper?.setText(R.id.mTvRechargeItemMoney, item?.recharge_record_price)
                helper?.setText(R.id.mTvRechargeItemTime, item?.recharge_record_times)
            }
        }

    override fun initView(view: View?) {
        super.initView(view)
        val topView = View(mContext)
        topView.layoutParams = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, resources.displayMetrics).toInt()
        )
        topView.backgroundColor = resources.getColor(R.color.grayBgColor)
        mBaseAdapter.addHeaderView(topView)
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, getBaseParamsWithPage(USER_MODULE, RECHARGERECORD_ACT, currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, getBaseParamsWithPage(USER_MODULE, RECHARGERECORD_ACT, currentPage))
    }

    override fun getEmptyMessage(): String {
        return "暂无充值记录"
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            processList(result as String, RechargeBean::class.java)
        }
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable())
        return itemDecoration
    }

}