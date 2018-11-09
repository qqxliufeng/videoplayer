package com.android.ql.lf.videoplayer.ui.activities

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.view.View
import com.android.ql.lf.baselibaray.ui.activity.BaseActivity
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.ui.fragments.player.PlayerInfoFragment
import com.shuyu.gsyvideoplayer.GSYBaseActivityDetail
import com.shuyu.gsyvideoplayer.GSYVideoManager
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer


/**
 * Created by lf on 18.11.9.
 * @author lf on 18.11.9
 */
class PlayerActivity : BaseActivity(), VideoAllCallBack {

    override fun onClickResumeFullscreen(url: String?, vararg objects: Any?) {
    }

    override fun onEnterFullscreen(url: String?, vararg objects: Any?) {
        //隐藏调全屏对象的返回按键
        val gsyVideoPlayer = objects[1] as GSYVideoPlayer
        gsyVideoPlayer.backButton.visibility = View.GONE
    }

    override fun onClickResume(url: String?, vararg objects: Any?) {
    }

    override fun onClickSeekbarFullscreen(url: String?, vararg objects: Any?) {
    }

    override fun onStartPrepared(url: String?, vararg objects: Any?) {
    }

    override fun onClickStartIcon(url: String?, vararg objects: Any?) {
    }

    override fun onTouchScreenSeekLight(url: String?, vararg objects: Any?) {
    }

    override fun onQuitFullscreen(url: String?, vararg objects: Any?) {
        orientationUtils?.backToProtVideo()
    }

    override fun onClickStartThumb(url: String?, vararg objects: Any?) {
    }

    override fun onEnterSmallWidget(url: String?, vararg objects: Any?) {
    }

    override fun onClickStartError(url: String?, vararg objects: Any?) {
    }

    override fun onClickBlankFullscreen(url: String?, vararg objects: Any?) {
    }

    override fun onPrepared(url: String?, vararg objects: Any?) {
        orientationUtils?.isEnable = true
        isPlay = true
    }

    override fun onAutoComplete(url: String?, vararg objects: Any?) {
    }

    override fun onQuitSmallWidget(url: String?, vararg objects: Any?) {
    }

    override fun onTouchScreenSeekVolume(url: String?, vararg objects: Any?) {
    }

    override fun onClickBlank(url: String?, vararg objects: Any?) {
    }

    override fun onClickStop(url: String?, vararg objects: Any?) {
    }

    override fun onClickSeekbar(url: String?, vararg objects: Any?) {
    }

    override fun onPlayError(url: String?, vararg objects: Any?) {
    }

    override fun onClickStopFullscreen(url: String?, vararg objects: Any?) {
    }

    override fun onTouchScreenSeekPosition(url: String?, vararg objects: Any?) {
    }

    companion object {
        fun startPlayerActivity(context: Context, vid: Int) {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("vid", vid)
            context.startActivity(intent)
        }
    }

    var isPlay: Boolean = false

    var isPause: Boolean = false

    private var orientationUtils: OrientationUtils? = null

    private val playerFragment: PlayerInfoFragment by lazy {
        PlayerInfoFragment.getPlayerInfoInstance(intent.getIntExtra("vid", 0))
    }

    override fun getLayoutId() = R.layout.activity_player_layout

    override fun initView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mFlPlayerContainer, playerFragment)
            .commit()
    }

    public fun initOrientation() {
        orientationUtils = OrientationUtils(this, playerFragment.getPlayer())
        orientationUtils?.isEnable = false
        if (playerFragment.getPlayer().fullscreenButton != null) {
            playerFragment.getPlayer().fullscreenButton.setOnClickListener {
                showFull()
            }
        }
    }

    private fun showFull() {
//        GSYBaseActivityDetail
        if (orientationUtils?.isLand != 1) {
            //直接横屏
            orientationUtils?.resolveByClick()
        }
        //第一个true是否需要隐藏actionbar，第二个true是否需要隐藏statusbar
        playerFragment.getPlayer().startWindowFullscreen(
            this,
            true,
            true
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //如果旋转了就全屏
        if (isPlay && !isPause) {
            playerFragment.getPlayer().onConfigurationChanged(
                this,
                newConfig,
                orientationUtils,
                true,
                true
            )
        }
    }

    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if (GSYVideoManager.backFromWindowFull(this)) {
            return
        }
        super.onBackPressed()
    }
}