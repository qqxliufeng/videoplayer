package com.android.ql.lf.videoplayer.ui.fragments.films

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.videoplayer.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import org.jetbrains.anko.backgroundColor

class FilmMenuFragment : BaseRecyclerViewFragment<String>(){

    override fun createAdapter(): BaseQuickAdapter<String, BaseViewHolder>  = object : BaseQuickAdapter<String,BaseViewHolder>(R.layout.adapter_menu_item_2_layout,mArrayList){
        override fun convert(helper: BaseViewHolder?, item: String?) {
            helper?.setText(R.id.mCtvMenuItem,item)
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(mContext,5)
    }

    override fun initView(view: View?) {
        super.initView(view)
        mSwipeRefreshLayout.backgroundColor = Color.WHITE
        setRefreshEnable(false)
        setLoadEnable(false)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable())
        return itemDecoration
    }

    override fun onRefresh() {
        super.onRefresh()
        testAdd("")
    }

}