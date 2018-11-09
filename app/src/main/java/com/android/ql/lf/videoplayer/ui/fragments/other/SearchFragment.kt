package com.android.ql.lf.videoplayer.ui.fragments.other

import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.videoplayer.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_search_layout.*

class SearchFragment : BaseRecyclerViewFragment<String>(){

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder> = object : BaseQuickAdapter<String,BaseViewHolder>(R.layout.adapter_search_item_layout,mArrayList) {
        override fun convert(helper: BaseViewHolder?, item: String?) {
        }
    }

    override fun getLayoutId() = R.layout.fragment_search_layout

    override fun initView(view: View?) {
        (mRlSearchContainer.layoutParams as ViewGroup.MarginLayoutParams).topMargin = statusBarHeight
        super.initView(view)
    }

}