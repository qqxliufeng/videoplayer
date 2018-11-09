package com.android.ql.lf.videoplayer.ui.fragments.films

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
import com.android.ql.lf.baselibaray.component.ApiParams
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.AbstractLazyLoadFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.vip.FilmBean
import com.android.ql.lf.videoplayer.ui.activities.PlayerActivity
import com.android.ql.lf.videoplayer.ui.fragments.other.SearchFragment
import com.android.ql.lf.videoplayer.ui.fragments.player.PlayerInfoFragment
import com.android.ql.lf.videoplayer.utils.VIDEO_LIST_ACT
import com.android.ql.lf.videoplayer.utils.VIDEO_MODULE
import com.android.ql.lf.videoplayer.utils.getBaseParamsWithPage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.youth.banner.BannerConfig
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_film_item_layout.*
import org.jetbrains.anko.bundleOf
import org.json.JSONObject

class FilmItemFragment : AbstractLazyLoadFragment<FilmBean>() {

    private val bannerList by lazy { arrayListOf<FilmBannerBean>() }
    private val noticeList by lazy { arrayListOf<FilmNoticeBean>() }

    companion object {
        fun newInstance(bannerJSON: String, classify: Int): FilmItemFragment {
            val filmItemFragment = FilmItemFragment()
            val bundle = bundleOf(Pair("json", bannerJSON), Pair("classify", classify))
            filmItemFragment.arguments = bundle
            return filmItemFragment
        }
    }


    override fun getLayoutId() = R.layout.fragment_film_item_layout

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

        arguments?.classLoader = this.javaClass.classLoader

        arguments?.getString("json")?.apply {
            val jsonObject = JSONObject(this)
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
            mBannerFilmItem.setImages(bannerList.map { it.slideshow_pic }).setDelayTime(3000)
                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR).setOnBannerListener {

            }.setIndicatorGravity(BannerConfig.CENTER).start()


            mMarqueeViewFileItem.startWithList(noticeList.map { it.notice_name })

        }

        mAlFilmItemContainer.addOnOffsetChangedListener { appBarLayout, verticalOffset ->
            mSwipeRefreshLayout.isEnabled = verticalOffset >= 0
        }

        (mRecyclerView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin =
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, resources.displayMetrics).toInt()

        (mRecyclerView.layoutParams as ViewGroup.MarginLayoutParams).rightMargin =
                (mRecyclerView.layoutParams as ViewGroup.MarginLayoutParams).leftMargin

        mLlFilmItemSearchContainer.setOnClickListener {
            FragmentContainerActivity.from(mContext).setNeedNetWorking(true).setTitle("").setHiddenToolBar(true)
                .setClazz(SearchFragment::class.java).start()
        }
    }

    override fun onStart() {
        super.onStart()
        mMarqueeViewFileItem.startFlipping()
    }

    override fun onStop() {
        super.onStop()
        mMarqueeViewFileItem.stopFlipping()
        mBannerFilmItem.stopAutoPlay()
    }

    override fun loadData() {
        isLoad = true
        mPresent.getDataByPost(
            0x0, getBaseParamsWithPage(VIDEO_MODULE, VIDEO_LIST_ACT, currentPage)
                .addParam("classify", arguments?.getInt("classify") ?: 0)
        )
    }

    override fun onLoadMore() {
        super.onLoadMore()
        loadData()
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            processList(result as String, FilmBean::class.java)
        }
    }


    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(mContext, 3)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        PlayerActivity.startPlayerActivity(mContext, mArrayList[position].video_id)
//        FragmentContainerActivity
//                .from(mContext)
//                .setNeedNetWorking(true)
//                .setTitle("")
//                .setExtraBundle(bundleOf(Pair("vid",mArrayList[position].video_id)))
//                .setClazz(PlayerInfoFragment::class.java)
//                .setHiddenToolBar(true)
//                .start()
    }

}

data class FilmBannerBean(
    val slideshow_id: Int,
    val slideshow_pic: String,
    val slideshow_vip: String,
    val slideshow_name: String,
    val slideshow_url: String
)

data class FilmNoticeBean(
    val notice_id: Int,
    val notice_name: String,
    val notice_content: String,
    val notice_url: String,
    val notice_vip: Int
)