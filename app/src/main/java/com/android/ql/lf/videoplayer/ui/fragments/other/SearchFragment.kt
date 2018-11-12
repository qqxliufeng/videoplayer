package com.android.ql.lf.videoplayer.ui.fragments.other

import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.baselibaray.utils.PreferenceUtils
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.user.UserInfo
import com.android.ql.lf.videoplayer.data.user.isLogin
import com.android.ql.lf.videoplayer.data.user.isVip
import com.android.ql.lf.videoplayer.data.vip.FilmBean
import com.android.ql.lf.videoplayer.ui.activities.PlayerActivity
import com.android.ql.lf.videoplayer.ui.fragments.mine.SelectLoginFragment
import com.android.ql.lf.videoplayer.ui.fragments.vip.ChargeVipFragment
import com.android.ql.lf.videoplayer.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.android.flexbox.FlexboxLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_search_layout.*
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast

class SearchFragment : BaseRecyclerViewFragment<FilmBean>() {

    private var searchItemList = arrayListOf<String>()

    override fun createAdapter(): BaseQuickAdapter<FilmBean, BaseViewHolder> =
        object : BaseQuickAdapter<FilmBean, BaseViewHolder>(R.layout.adapter_search_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: FilmBean?) {
                GlideManager.loadImage(mContext, item?.video_pic, helper?.getView(R.id.mIvSearchItemPic))
                helper?.setText(R.id.mTvSearchItemTitle, item?.video_name)
                helper?.setText(R.id.mTvSearchItemDes, item?.video_desc)
                helper?.addOnClickListener(R.id.mBtSearchItemPlay)
                when (item?.video_type) {
                    2 -> { // 综艺
                        if (item.video_gather!=null && item.video_gather.size > 3) {
                            helper?.setGone(R.id.mTvSearchItemCollectionSeeMore, true)
                        }else{
                            helper?.setGone(R.id.mTvSearchItemCollectionSeeMore, false)
                        }
                        helper?.setGone(R.id.mFlSearchItemCollection, true)
                        addItem(helper, item, ViewGroup.LayoutParams.MATCH_PARENT)
                    }
                    1 -> {// 电视剧
                        if (item.video_gather!=null && item.video_gather.size > 3) {
                            helper?.setGone(R.id.mTvSearchItemCollectionSeeMore, true)
                        }else{
                            helper?.setGone(R.id.mTvSearchItemCollectionSeeMore, false)
                        }
                        helper?.setGone(R.id.mFlSearchItemCollection, true)
                        addItem(helper, item, ViewGroup.LayoutParams.WRAP_CONTENT)
                    }
                    else -> {
                        helper?.setGone(R.id.mTvSearchItemCollectionSeeMore, false)
                        helper?.setGone(R.id.mFlSearchItemCollection, false)
                    }
                }
            }

            fun addItem(helper: BaseViewHolder?, item: FilmBean, width: Int) {
                item.video_gather.forEach {
                    val textView = TextView(mContext)
                    val params = FlexboxLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT)
                    if (width == ViewGroup.LayoutParams.WRAP_CONTENT) {
                        params.leftMargin =
                                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, resources.displayMetrics)
                                    .toInt()
                        params.rightMargin = params.leftMargin
                    } else {
                        params.topMargin =
                                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5.0f, resources.displayMetrics)
                                    .toInt()
                        params.bottomMargin = params.topMargin
                    }
                    textView.layoutParams = params
                    textView.setBackgroundResource(R.drawable.shape_ctv_bg1)
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.grayTextColor))
                    textView.text = it.collected_name
                    helper?.getView<FlexboxLayout>(R.id.mFlSearchItemCollection)?.addView(textView)
                }
            }
        }

    override fun getLayoutId() = R.layout.fragment_search_layout

    override fun initView(view: View?) {
        (mRlSearchContainer.layoutParams as ViewGroup.MarginLayoutParams).topMargin = statusBarHeight
        super.initView(view)
        setRefreshEnable(false)
        val historySearchJson = PreferenceUtils.getPrefString(mContext, "search_content", "")
        if (historySearchJson != "") {
            searchItemList.addAll(Gson().fromJson<ArrayList<String>>(historySearchJson))
            searchItemList.forEach {
                val textView = TextView(mContext)
                val params =
                    FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                params.rightMargin =
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, resources.displayMetrics).toInt()
                params.topMargin = params.rightMargin
                params.bottomMargin = params.rightMargin
                textView.layoutParams = params
                textView.setBackgroundResource(R.drawable.shape_tv_7)
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.grayTextColor))
                textView.text = it
                textView.setOnClickListener { _ ->
                    mEtSearchContent.setText(textView.text)
                    searchItemList.remove(mEtSearchContent.getTextString())
                    searchItemList.add(0, mEtSearchContent.getTextString())
                    PreferenceUtils.setPrefString(mContext, "search_content", Gson().toJson(searchItemList))
                    search()
                }
                mFlSearchHistory.addView(textView)
            }
        }
        mTvSearch.setOnClickListener {
            if (mEtSearchContent.isEmpty()) {
                toast("请输入要搜索的内容")
                return@setOnClickListener
            }
            hiddenSoftInput(mEtSearchContent)
            if (searchItemList.contains(mEtSearchContent.getTextString())) {
                searchItemList.remove(mEtSearchContent.getTextString())
            }
            searchItemList.add(0, mEtSearchContent.getTextString())
            PreferenceUtils.setPrefString(mContext, "search_content", Gson().toJson(searchItemList))
            search()
        }
        mTvSearchHistoryDelete.setOnClickListener {
            if (mFlSearchHistory.childCount > 0) {
                alert("是否要清空搜索历史？", title = null, init = {
                    positiveButton("确定") {
                        searchItemList.clear()
                        PreferenceUtils.setPrefString(mContext, "search_content", "")
                        mFlSearchHistory.removeAllViews()
                    }
                    negativeButton("取消") {}
                }).build().show()
            }
        }
    }

    private fun search() {
        mArrayList.clear()
        mSwipeRefreshLayout.visibility = View.VISIBLE
        mLlSearchHistoryContainer.visibility = View.GONE
        mSwipeRefreshLayout.post {
            mSwipeRefreshLayout.isRefreshing = true
            mPresent.getDataByPost(
                0x0, getBaseParamsWithPage(VIDEO_MODULE, VIDEO_LIST_ACT, currentPage)
                    .addParam("classify", arguments?.getInt("classify") ?: 0)
                    .addParam("keyword", mEtSearchContent.getTextString())
                    .addParam("type", arguments?.getInt("type") ?: 0)
            )
        }
    }

    override fun getEmptyMessage(): String {
        return "暂无搜索内容~~"
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            processList(result as String, FilmBean::class.java)
        }
    }

    override fun onMyItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (view?.id == R.id.mBtSearchItemPlay) {
            if (UserInfo.isLogin()) {
                if (mArrayList[position].video_vip == 2) {
                    if (UserInfo.isVip()) {
                        PlayerActivity.startPlayerActivity(mContext, mArrayList[position].video_id)
                    } else {
                        ChargeVipFragment.startOpenVip(mContext)
                    }
                } else {
                    PlayerActivity.startPlayerActivity(mContext, mArrayList[position].video_id)
                }
            } else {
                SelectLoginFragment.startSelectLoginFragment(mContext)
            }
        }
    }
}