package com.android.ql.lf.videoplayer.ui.fragments.player

import android.annotation.SuppressLint
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.android.ql.lf.baselibaray.component.ApiParams
import com.android.ql.lf.baselibaray.ui.activity.FragmentContainerActivity
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.baselibaray.utils.RxBus
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.vip.FilmBean
import com.android.ql.lf.videoplayer.ui.activities.PlayerActivity
import com.android.ql.lf.videoplayer.ui.fragments.films.FilmIntroduceDialogFragment
import com.android.ql.lf.videoplayer.ui.fragments.films.FilmMenuFragment
import com.android.ql.lf.videoplayer.ui.fragments.vip.ChargeVipFragment
import com.android.ql.lf.videoplayer.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.listener.OnItemClickListener
import com.google.gson.Gson
import com.shuyu.gsyvideoplayer.video.GSYSampleADVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer
import kotlinx.android.synthetic.main.fragment_player_info_layout.*
import org.jetbrains.anko.bundleOf
import org.jetbrains.anko.support.v4.toast
import org.json.JSONObject

class PlayerInfoFragment : BaseRecyclerViewFragment<FilmBean>() {

    companion object {
        fun getPlayerInfoInstance(vid: Int): PlayerInfoFragment {
            val playerInfoFragment = PlayerInfoFragment()
            playerInfoFragment.arguments = bundleOf(Pair("vid", vid))
            return playerInfoFragment
        }
    }

    private val mCollectionList = arrayListOf<VideoCollectionBean>()
    private val mAdList = arrayListOf<VideoAdBean>()
    private var mCurrentVideoInfo: FilmBean? = null
    private var mCurrentVideoCollectionBean: VideoCollectionBean? = null

    private var mVideoId: Int? = null

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

    public fun getPlayer(): GSYBaseVideoPlayer = mVideoPlayer

    private val menuSubscription by lazy {
        RxBus.getDefault().toObservable(String::class.java).subscribe {
            val index = it.toInt() - 1
            val temp = mCollectionList[index]
            if (temp.collected_url == mCurrentVideoCollectionBean?.collected_url ?: "" && index == mCollectionList.indexOf(
                    mCurrentVideoCollectionBean
                )
            ) {
                return@subscribe
            }
            play(index)
        }
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
        mVideoId = arguments?.getInt("vid")
        super.initView(view)
        menuSubscription
        initVideo()
        setLoadEnable(false)
        setRefreshEnable(false)
        mRvPlayerInfoMenu.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        mRvPlayerInfoMenu.adapter = mMenuAdapter
        mRvPlayerInfoMenu.addOnItemTouchListener(object : OnItemClickListener() {
            override fun onSimpleItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
                val temp = mCollectionList[position]
                if (temp.collected_url == mCurrentVideoCollectionBean?.collected_url ?: "") {
                    return
                }
                play(position)
            }
        })
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
            ChargeVipFragment.startOpenVip(mContext)
        }
        mVideoPlayer.isVip(false)
        mVideoPlayer.setIsTouchWiget(true)
        //关闭自动旋转
        mVideoPlayer.isRotateViewAuto = false
        mVideoPlayer.isLockLand = false
        mVideoPlayer.isShowFullAnimation = false
        mVideoPlayer.isNeedLockFull = true
        mVideoPlayer.setVideoAllCallBack(mContext as PlayerActivity)
        (mContext as PlayerActivity).initOrientation()
    }

    override fun onStop() {
        super.onStop()
        mVideoPlayer.currentPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        mVideoPlayer.currentPlayer.onVideoResume(false)
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在加入看单……")
        }
    }

    override fun onRefresh() {
        mPresent.getDataByPost(
            0x0, getBaseParamsWithModAndAct(VIDEO_MODULE, VIDEO_DETAIL_ACT)
                .addParam("vid", mVideoId)
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

            mIntroduceDialogFragment.setData(
                mCurrentVideoInfo?.video_name,
                mCurrentVideoInfo?.video_crumbs,
                mCurrentVideoInfo?.video_desc,
                mCurrentVideoInfo?.video_intro
            )

            mTvPlayerInfoName.text = mCurrentVideoInfo?.video_name
            mTvPlayerInfoCrumbs.text = mCurrentVideoInfo?.video_crumbs
            mTvPlayerInfoMenuCount.text = "全${mCurrentVideoInfo?.video_count}集"
            mTvPlayerInfoMenuCount.setOnClickListener {
                FragmentContainerActivity.from(mContext).setClazz(FilmMenuFragment::class.java).setExtraBundle(
                    bundleOf(
                        Pair("collection", mCollectionList.size)
                    )
                ).setTitle("剧集")
                    .setNeedNetWorking(true).start()
            }
            mBaseAdapter.notifyDataSetChanged()
            play(0)
        }
    }

    private fun play(index: Int) {
        val playerList = arrayListOf<GSYSampleADVideoPlayer.GSYADVideoModel>()
        if (!mAdList.isEmpty() && !mCollectionList.isEmpty()) {
            playerList.add(
                GSYSampleADVideoPlayer.GSYADVideoModel(
                    mAdList[0].ad_url,
                    "",
                    GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD
                )
            )
            mCollectionList.forEach { it.isChecked = false }
            mCollectionList[index].isChecked = true
            mCurrentVideoCollectionBean = mCollectionList[index]
            playerList.add(
                GSYSampleADVideoPlayer.GSYADVideoModel(
                    mCollectionList[index].collected_url,
                    mCollectionList[index].collected_name,
                    GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_NORMAL
                )
            )
        }
        mVideoPlayer.setAdUp(playerList, true, 0)
        mMenuAdapter.notifyDataSetChanged()
        val thumbImageView = ImageView(mContext)
        GlideManager.loadImage(mContext, mCurrentVideoCollectionBean?.collected_pic, thumbImageView)
        mVideoPlayer.thumbImageView = thumbImageView
        mPresent.getDataByPost(
            0x2,
            getBaseParamsWithModAndAct(VIDEO_MODULE, VIDEO_PLAY_ACT).addParam(
                "vid",
                mCurrentVideoCollectionBean?.collected_id
            )
        )
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check != null) {
                toast((check.obj as JSONObject).optString(MSG_FLAG))
            }
        } else if (requestID == 0x2) {
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                mVideoPlayer.startPlayLogic()
            }
        } else {
            super.onRequestSuccess(requestID, result)
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        mVideoId = mArrayList[position].video_id
        reset()
        onRefresh()
    }

    private fun reset() {
        mArrayList.clear()
        mCollectionList.clear()
        mAdList.clear()
        mCurrentVideoInfo = null
        mCurrentVideoCollectionBean = null
        mBaseAdapter.notifyDataSetChanged()
        mMenuAdapter.notifyDataSetChanged()
    }

    override fun showFailMessage(requestID: Int): String {
        if (requestID == 0x1) {
            return "加入看单失败！"
        }
        return super.showFailMessage(requestID)
    }

    override fun onDestroyView() {
        unsubscribe(menuSubscription)
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