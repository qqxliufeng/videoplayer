package com.android.ql.lf.videoplayer.ui.widgets;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import com.android.ql.lf.videoplayer.R;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.video.GSYSampleADVideoPlayer;
import com.shuyu.gsyvideoplayer.video.ListGSYVideoPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QLGSYVideoPlayer extends ListGSYVideoPlayer {

    public interface OnOpenVipListener {
        public void openVip();
    }

    protected View mJumpAd;

    protected ViewGroup mWidgetContainer;

    protected TextView mADTime;

    protected boolean isAdModel = false;

    protected boolean isFirstPrepared = false;

    protected boolean isVip = false;

    private OnOpenVipListener onOpenVipListener;

    public QLGSYVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public QLGSYVideoPlayer(Context context) {
        super(context);
    }

    public QLGSYVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
        mJumpAd = findViewById(R.id.jump_ad);
        mJumpAd.setBackgroundColor(Color.parseColor("#55000000"));
        mADTime = (TextView) findViewById(R.id.ad_time);
        mADTime.setBackgroundColor(Color.parseColor("#55000000"));
        ((TextView) mJumpAd).setText("VIP可关闭广告");
        mWidgetContainer = (ViewGroup) findViewById(R.id.widget_container);
        findViewById(R.id.fullscreen).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                startWindowFullscreen(getContext(),false,false);
            }
        });
        if (mJumpAd != null) {
            mJumpAd.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isVip) {
                        playNext();
                    } else {
                        if (onOpenVipListener != null) {
                            onOpenVipListener.openVip();
                        }
                    }
                }
            });
        }
        getBackButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Activity)getContext()).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    ((Activity)getContext()).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }else {
                    ((Activity)getContext()).finish();
                }
            }
        });
    }


    public void isVip(boolean isVip) {
        this.isVip = isVip;
    }

    public void setOnOpenVipListener(OnOpenVipListener onOpenVipListener) {
        this.onOpenVipListener = onOpenVipListener;
    }

    @Override
    public int getLayoutId() {
        return com.shuyu.gsyvideoplayer.R.layout.video_layout_sample_ad;
    }

    /**
     * 如果需要片头广告的，请用setAdUp
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param position      需要播放的位置
     * @return
     */
    @Override
    public boolean setUp(List<GSYVideoModel> url, boolean cacheWithPlay, int position) {
        return setUp(url, cacheWithPlay, position, null);
    }

    /**
     * 如果需要片头广告的，请用setAdUp
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param position      需要播放的位置
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @return
     */
    @Override
    public boolean setUp(List<GSYVideoModel> url, boolean cacheWithPlay, int position, File cachePath) {
        return setUp(url, cacheWithPlay, position, cachePath, new HashMap<String, String>());
    }

    /**
     * 如果需要片头广告的，请用setAdUp
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param position      需要播放的位置
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param mapHeadData   http header
     * @return
     */
    @Override
    public boolean setUp(List<GSYVideoModel> url, boolean cacheWithPlay, int position, File cachePath, Map<String, String> mapHeadData) {
        return setUp(url, cacheWithPlay, position, cachePath, mapHeadData, true);
    }

    /**
     * 如果需要片头广告的，请用setAdUp
     *
     * @param url           播放url
     * @param cacheWithPlay 是否边播边缓存
     * @param position      需要播放的位置
     * @param cachePath     缓存路径，如果是M3U8或者HLS，请设置为false
     * @param mapHeadData   http header
     * @param changeState   切换的时候释放surface
     * @return
     */
    @Override
    protected boolean setUp(List<GSYVideoModel> url, boolean cacheWithPlay, int position, File cachePath, Map<String, String> mapHeadData, boolean changeState) {
        GSYVideoModel gsyVideoModel = url.get(position);
        if (gsyVideoModel instanceof GSYSampleADVideoPlayer.GSYADVideoModel) {
            GSYSampleADVideoPlayer.GSYADVideoModel gsyadVideoModel = (GSYSampleADVideoPlayer.GSYADVideoModel) gsyVideoModel;
            if (gsyadVideoModel.isSkip() && position < (url.size() - 1)) {
                return setUp(url, cacheWithPlay, position + 1, cachePath, mapHeadData, changeState);
            }
            isAdModel = (gsyadVideoModel.getType() == GSYSampleADVideoPlayer.GSYADVideoModel.TYPE_AD);
        }
        changeAdUIState();
        return super.setUp(url, cacheWithPlay, position, cachePath, mapHeadData, changeState);
    }

    @Override
    public void onPrepared() {
        super.onPrepared();
        isFirstPrepared = true;
        changeAdUIState();
    }

    @Override
    protected void updateStartImage() {
        if (mStartButton != null) {
            if (mStartButton instanceof ImageView) {
                ImageView imageView = (ImageView) mStartButton;
                if (mCurrentState == CURRENT_STATE_PLAYING) {
                    imageView.setImageResource(com.shuyu.gsyvideoplayer.R.drawable.video_click_pause_selector);
                } else if (mCurrentState == CURRENT_STATE_ERROR) {
                    imageView.setImageResource(com.shuyu.gsyvideoplayer.R.drawable.video_click_play_selector);
                } else {
                    imageView.setImageResource(com.shuyu.gsyvideoplayer.R.drawable.video_click_play_selector);
                }
            }
        }
    }

    /**
     * 广告期间不需要双击
     */
    @Override
    protected void touchDoubleUp() {
        if (isAdModel) {
            return;
        }
        super.touchDoubleUp();
    }

    /**
     * 广告期间不需要触摸
     */
    @Override
    protected void touchSurfaceMove(float deltaX, float deltaY, float y) {
        if (mChangePosition && isAdModel) {
            return;
        } else {
            super.touchSurfaceMove(deltaX, deltaY, y);
        }
    }

    /**
     * 广告期间不需要触摸
     */
    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        if ((absDeltaX > mThreshold || absDeltaY > mThreshold)) {
            int screenWidth = CommonUtil.getScreenWidth(getContext());
            if (isAdModel && absDeltaX >= mThreshold && Math.abs(screenWidth - mDownX) > mSeekEndOffset) {
                //防止全屏虚拟按键
                mChangePosition = true;
                mDownPosition = getCurrentPositionWhenPlaying();
            } else {
                super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
            }
        }
    }

    /**
     * 广告期间不需要触摸
     */
    @Override
    protected void touchSurfaceUp() {
        if (mChangePosition && isAdModel) {
            return;
        }
        super.touchSurfaceUp();

    }


    @Override
    protected void hideAllWidget() {
        if (isFirstPrepared && isAdModel) {
            return;
        }
        super.hideAllWidget();
    }

    @Override
    protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
        if (mADTime != null && currentTime > 0) {
            int totalSeconds = totalTime / 1000;
            int currentSeconds = currentTime / 1000;
            mADTime.setText("" + (totalSeconds - currentSeconds));
        }
    }


    /**
     * 根据是否广告url修改ui显示状态
     */
    protected void changeAdUIState() {
        if (mJumpAd != null) {
            mJumpAd.setVisibility((isFirstPrepared && isAdModel) ? VISIBLE : GONE);
        }
        if (mADTime != null) {
            mADTime.setVisibility((isFirstPrepared && isAdModel) ? VISIBLE : GONE);
        }
        if (mWidgetContainer != null) {
            mWidgetContainer.setVisibility((isFirstPrepared && isAdModel) ? GONE : VISIBLE);
        }
        if (mBottomContainer != null) {
            int color = (isFirstPrepared && isAdModel) ? Color.TRANSPARENT : getContext().getResources().getColor(com.shuyu.gsyvideoplayer.R.color.bottom_container_bg);
            mBottomContainer.setBackgroundColor(color);
        }
        if (mCurrentTimeTextView != null) {
            mCurrentTimeTextView.setVisibility((isFirstPrepared && isAdModel) ? INVISIBLE : VISIBLE);
        }
        if (mTotalTimeTextView != null) {
            mTotalTimeTextView.setVisibility((isFirstPrepared && isAdModel) ? INVISIBLE : VISIBLE);
        }
        if (mProgressBar != null) {
            mProgressBar.setVisibility((isFirstPrepared && isAdModel) ? INVISIBLE : VISIBLE);
            mProgressBar.setEnabled(!(isFirstPrepared && isAdModel));
        }
    }


    /******************对外接口*******************/

    /**
     * 带片头广告的，setAdUp
     *
     * @param url
     * @param cacheWithPlay
     * @param position
     * @return
     */
    public boolean setAdUp(ArrayList<GSYSampleADVideoPlayer.GSYADVideoModel> url, boolean cacheWithPlay, int position) {
        return setUp((ArrayList<GSYVideoModel>) url.clone(), cacheWithPlay, position);
    }

    /**
     * 带片头广告的，setAdUp
     *
     * @param url
     * @param cacheWithPlay
     * @param position
     * @param cachePath
     * @return
     */
    public boolean setAdUp(ArrayList<GSYSampleADVideoPlayer.GSYADVideoModel> url, boolean cacheWithPlay, int position, File cachePath) {
        return setUp((ArrayList<GSYVideoModel>) url.clone(), cacheWithPlay, position, cachePath);
    }

    /**
     * 带片头广告的，setAdUp
     *
     * @param url
     * @param cacheWithPlay
     * @param position
     * @param cachePath
     * @param mapHeadData
     * @return
     */
    public boolean setAdUp(ArrayList<GSYSampleADVideoPlayer.GSYADVideoModel> url, boolean cacheWithPlay, int position, File cachePath, Map<String, String> mapHeadData) {
        return setUp((ArrayList<GSYVideoModel>) url.clone(), cacheWithPlay, position, cachePath, mapHeadData);
    }
}
