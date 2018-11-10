package com.android.ql.lf.videoplayer.ui.fragments.bottom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.user.UserInfo
import com.android.ql.lf.videoplayer.data.user.isLogin
import com.android.ql.lf.videoplayer.data.user.isVip
import com.android.ql.lf.videoplayer.data.vip.FilmBean
import com.android.ql.lf.videoplayer.ui.activities.PlayerActivity
import com.android.ql.lf.videoplayer.ui.fragments.films.FilmBannerBean
import com.android.ql.lf.videoplayer.ui.fragments.films.FilmNoticeBean
import com.android.ql.lf.videoplayer.ui.fragments.mine.SelectLoginFragment
import com.android.ql.lf.videoplayer.ui.fragments.vip.ChargeVipFragment
import com.android.ql.lf.videoplayer.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_bottom_vip_layout.*
import org.json.JSONObject

class BottomVipFragment : BaseRecyclerViewFragment<FilmBean>() {

    private val bannerList by lazy { arrayListOf<FilmBannerBean>() }
    private val noticeList by lazy { arrayListOf<FilmNoticeBean>() }

    override fun getLayoutId() = R.layout.fragment_bottom_vip_layout

    override fun createAdapter() =
        object : BaseQuickAdapter<FilmBean, BaseViewHolder>(R.layout.adapter_film_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: FilmBean?) {
                GlideManager.loadImage(mContext, item?.video_pic, helper?.getView(R.id.mIvFilmItemPic))
                helper?.setText(R.id.mTvFilmItemQuality, item?.video_definition)
                helper?.setText(R.id.mTvFilmItemGrade, item?.video_grade)
                helper?.setText(R.id.mTvFilmItemName, item?.video_name)
                helper?.setText(R.id.mTvFilmItemContent, item?.video_desc)
            }
        }

    override fun initView(view: View?) {
        super.initView(view)
        mAlVipContainer.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            mSwipeRefreshLayout.isEnabled = verticalOffset >= 0
        }
        (mRecyclerView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, resources.displayMetrics).toInt()
        (mRecyclerView.layoutParams as ViewGroup.MarginLayoutParams).rightMargin =
                (mRecyclerView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin
        mIvVipRecharge.doClickWithUserStatusStart("") {
            ChargeVipFragment.startOpenVip(mContext)
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(
            0x0,
            getBaseParamsWithPage(VIDEO_MODULE, VIDEO_LIST_ACT, currentPage).addParam("vip", "2")
        )
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(
            0x0,
            getBaseParamsWithPage(VIDEO_MODULE, VIDEO_LIST_ACT, currentPage).addParam("vip", "2")
        )
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(mContext, 3)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }

    override fun onRequestEnd(requestID: Int) {
        super.onRequestEnd(requestID)
        if (requestID == 0x0) {
            mPresent.getDataByPost(0x1, getBaseParamsWithModAndAct(VIDEO_MODULE, VIDEO_NAV_ACT).addParam("vip", "2"))
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            processList(result as String, FilmBean::class.java)
        } else if (requestID == 0x1) {
            super.onRequestSuccess(requestID, result)
        }
    }

    override fun onHandleSuccess(requestID: Int, obj: Any?) {
        if (obj != null) {
            val jsonObject = obj as JSONObject
            val bannerJsonArray = jsonObject.optJSONArray("carousel")
            (0 until bannerJsonArray.length()).forEach {
                bannerList.add(
                    Gson().fromJson(
                        bannerJsonArray.optJSONObject(it).toString(),
                        FilmBannerBean::class.java
                    )
                )
            }
            val noticeJsonArray = jsonObject.optJSONArray("notice")
            (0 until noticeJsonArray.length()).forEach {
                noticeList.add(
                    Gson().fromJson(
                        noticeJsonArray.optJSONObject(it).toString(),
                        FilmNoticeBean::class.java
                    )
                )
            }
            mBannerFilmItem.setImageLoader(object : ImageLoader() {
                override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
                    imageView?.scaleType = ImageView.ScaleType.CENTER_CROP
                    GlideManager.loadImage(mContext, path as String, imageView)
                }
            })
            mBannerFilmItem.setImages(bannerList.map { it.slideshow_pic }).setDelayTime(3000).setBannerStyle(
                BannerConfig.CIRCLE_INDICATOR
            ).setOnBannerListener {
            }.setIndicatorGravity(BannerConfig.CENTER).start()
            mMarqueeViewFileItem.startWithList(noticeList.map { it.notice_name })
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        if (UserInfo.isLogin()) {
            if (UserInfo.isVip()) {
                PlayerActivity.startPlayerActivity(mContext, mArrayList[position].video_id)
            } else {
                ChargeVipFragment.startOpenVip(mContext)
            }
        } else {
            SelectLoginFragment.startSelectLoginFragment(mContext)
        }
    }
}