package com.android.ql.lf.videoplayer.ui.fragments.player

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.baselibaray.component.ApiParams
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.vip.FilmBean
import com.android.ql.lf.videoplayer.ui.fragments.films.FilmIntroduceDialogFragment
import com.android.ql.lf.videoplayer.ui.fragments.films.FilmMenuFragment
import com.android.ql.lf.videoplayer.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.shuyu.gsyvideoplayer.video.GSYSampleADVideoPlayer
import kotlinx.android.synthetic.main.fragment_player_info_layout.*
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class PlayerInfoFragment : BaseRecyclerViewFragment<FilmBean>() {


    private val mCollectionList = arrayListOf<VideoCollectionBean>()
    private val mAdList = arrayListOf<VideoAdBean>()
    private var mCurrentVideoInfo: FilmBean? = null
    private var mCurrentVideoCollectionBean: VideoCollectionBean? = null

    private val mMenuAdapter: BaseQuickAdapter<VideoCollectionBean, BaseViewHolder>  by lazy {
        object :
            BaseQuickAdapter<VideoCollectionBean, BaseViewHolder>(R.layout.adapter_menu_item_layout, mCollectionList) {
            override fun convert(helper: BaseViewHolder?, item: VideoCollectionBean?) {
                helper?.setText(R.id.mCtvMenuItem, "${helper.adapterPosition + 1}")
                helper?.setChecked(R.id.mCtvMenuItem, item!!.isChecked)
            }
        }
    }

    private val mIntroduceDialogFragment: FilmIntroduceDialogFragment by lazy {
        FilmIntroduceDialogFragment()
    }


    override fun createAdapter(): BaseQuickAdapter<FilmBean, BaseViewHolder> =
        object : BaseQuickAdapter<FilmBean, BaseViewHolder>(R.layout.adapter_player_info_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: FilmBean?) {
                GlideManager.loadImage(mContext, item?.video_pic, helper?.getView(R.id.mIvPlayerInfoItemPic))
                helper?.setText(R.id.mTvPlayerInfoItemName, item?.video_name)
            }
        }

    override fun getLayoutId() = R.layout.fragment_player_info_layout

    override fun initView(view: View?) {
        super.initView(view)
        initVideo()
        setLoadEnable(false)
        setRefreshEnable(false)
        mRvPlayerInfoMenu.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvPlayerInfoMenu.adapter = mMenuAdapter
        mTvPlayerInfoIntroduce.setOnClickListener {
            mIntroduceDialogFragment.show(childFragmentManager, "film_introduce_dialog")
        }
        mTvPlayerInfoAddMenu.doClickWithUserStatusStart("") {
            mPresent.getDataByPost(
                0x1,
                getBaseParamsWithModAndAct(VIDEO_MODULE, VIDEO_WATCHING_ACT).addParam(
                    "theme", mCurrentVideoCollectionBean?.collected_theme ?: ""
                ).addParam("vid", mCurrentVideoCollectionBean?.collected_id ?: "")
            )
        }
    }

    private fun initVideo() {
        (mVideoPlayer.layoutParams as ViewGroup.MarginLayoutParams).topMargin = statusBarHeight
        mVideoPlayer.setOnOpenVipListener {
            toast("开通VIP")
        }
        mVideoPlayer.isVip(false)
        mVideoPlayer.backButton.setOnClickListener { finish() }
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在加入看单……")
        }
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(
            0x0, getBaseParamsWithModAndAct(VIDEO_MODULE, VIDEO_DETAIL_ACT)
                .addParam("vid", arguments?.getInt("vid"))
        )
    }

    @SuppressLint("SetTextI18n")
    override fun onHandleSuccess(requestID: Int, obj: Any?) {
        super.onHandleSuccess(requestID, obj)
        if (requestID == 0x0) {
            mCurrentVideoInfo = Gson().fromJson<FilmBean>((obj as JSONObject).optJSONObject("video").toString())

            val tempCollectionList = obj.optJSONArray("collected").toArrayList<VideoCollectionBean>()
            mCollectionList.addAll(tempCollectionList ?: arrayListOf())

            val tempVideoList = obj.optJSONArray("recommend").toArrayList<FilmBean>()
            mArrayList.addAll(tempVideoList ?: arrayListOf<FilmBean>())

            val tempAdList = obj.optJSONArray("ad").toArrayList<VideoAdBean>()
            if (tempAdList != null) {
                mAdList.addAll(tempAdList)
            }

            mTvPlayerInfoName.text = mCurrentVideoInfo?.video_name
            mTvPlayerInfoCrumbs.text = mCurrentVideoInfo?.video_crumbs
            mTvPlayerInfoMenuCount.text = "全${mCurrentVideoInfo?.video_count}集"
            mTvPlayerInfoMenuCount.setOnClickListener {
                FragmentContainerActivity.from(mContext).setClazz(FilmMenuFragment::class.java).setTitle("剧集")
                    .setNeedNetWorking(true).start()
            }

            mBaseAdapter.notifyDataSetChanged()

            val playerList = arrayListOf<GSYSampleADVideoPlayer.GSYADVideoModel>()
            if (!mAdList.isEmpty() && !mCollectionList.isEmpty()) {
                playerList.add(
                    GSYSampleADVideoPlayer.GSYADVideoModel(
                        mAdList[0].ad_url,
                        "",
                        GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD
                    )
                )
                mCollectionList[0].isChecked = true
                mCurrentVideoCollectionBean = mCollectionList[0]
                playerList.add(
                    GSYSampleADVideoPlayer.GSYADVideoModel(
                        mCollectionList[0].collected_url,
                        mCollectionList[0].collected_name,
                        GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL
                    )
                )
            }
            mVideoPlayer.setAdUp(playerList, true, 0)
            mMenuAdapter.notifyDataSetChanged()
            mIntroduceDialogFragment.setData(
                mCurrentVideoInfo?.video_name,
                mCurrentVideoInfo?.video_crumbs,
                mCurrentVideoInfo?.video_desc,
                mCurrentVideoInfo?.video_intro
            )
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null) {
                toast((check.obj as JSONObject).optString(MSG_FLAG))
            }
        } else {
            super.onRequestSuccess(requestID, result)
        }
    }

    override fun showFailMessage(requestID: Int): String {
        if (requestID == 0x1) {
            return "加入看单失败！"
        }
        return super.showFailMessage(requestID)
    }

    override fun onDestroyView() {
        mVideoPlayer.release()
        super.onDestroyView()
    }
}


data class VideoCollectionBean(
    val collected_id: Int,
    val collected_name: String,
    val collected_pic: String,
    val collected_url: String,
    val collected_theme: Int,
    val collected_orders: String,
    val collected_play: Int,
    val collected_fpic: String
) {
    var isChecked: Boolean = false
}

data class VideoAdBean(
    val ad_id: Int,
    val ad_duration: Int,
    val ad_url: String,
    val ad_pic: String,
    val ad_name: String
)