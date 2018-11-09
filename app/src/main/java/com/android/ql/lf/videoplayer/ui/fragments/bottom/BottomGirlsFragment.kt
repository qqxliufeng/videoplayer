package com.android.ql.lf.videoplayer.ui.fragments.bottom

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import android.view.ViewGroup
import android.widget.TextView
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.girls.GirlsAdBean
import com.android.ql.lf.videoplayer.data.girls.GirlsBean
import com.android.ql.lf.videoplayer.utils.VIDEO_BELLE_ACT
import com.android.ql.lf.videoplayer.utils.VIDEO_MODULE
import com.android.ql.lf.videoplayer.utils.fromJson
import com.android.ql.lf.videoplayer.utils.getBaseParamsWithPage
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_bottom_girls_layout.*
import org.jetbrains.anko.backgroundDrawable
import org.json.JSONObject

class BottomGirlsFragment : BaseRecyclerViewFragment<GirlsBean>() {

    private var mGirlsAdBean: GirlsAdBean? = null

    override fun getLayoutId() = R.layout.fragment_bottom_girls_layout

    override fun createAdapter(): BaseQuickAdapter<GirlsBean, BaseViewHolder> =
        object : BaseQuickAdapter<GirlsBean, BaseViewHolder>(R.layout.adapter_girls_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: GirlsBean?) {
                val tagView = helper?.getView<TextView>(R.id.mTvGirlsItemTag)
                when (helper?.layoutPosition!! % 4) {
                    0 -> {
                        tagView?.backgroundDrawable?.setTint(Color.parseColor("#bc31ff"))
                    }
                    1 -> {
                        tagView?.backgroundDrawable?.setTint(Color.parseColor("#ff578e"))
                    }
                    2 -> {
                        tagView?.backgroundDrawable?.setTint(Color.parseColor("#ffab31"))
                    }
                    3 -> {
                        tagView?.backgroundDrawable?.setTint(Color.parseColor("#ff3a5e"))
                    }
                }
                helper.setText(R.id.mTvGirlsItemTag, item?.belle_tag)
                helper.setText(R.id.mTvGirlsItemName, item?.belle_name)
                GlideManager.loadImage(mContext, item?.belle_pic ?: "", helper.getView(R.id.mIvGirlItemPic))
            }
        }

    override fun initView(view: View?) {
        super.initView(view)
        (mIvGirlTopAd.layoutParams as ViewGroup.MarginLayoutParams).topMargin = statusBarHeight
    }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, getBaseParamsWithPage(VIDEO_MODULE, VIDEO_BELLE_ACT, currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, getBaseParamsWithPage(VIDEO_MODULE, VIDEO_BELLE_ACT, currentPage))
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            processList(result as String, GirlsBean::class.java)
            val check = checkResultCode(result)
            if (check != null && check.code == SUCCESS_CODE) {
                mGirlsAdBean = Gson().fromJson<GirlsAdBean>((check.obj as JSONObject).optJSONObject("arr").toString())
                GlideManager.loadImage(mContext, mGirlsAdBean?.bellead_pic ?: "", mIvGirlTopAd)
                mIvGirlTopAd.setOnClickListener {  }
                GlideManager.loadFaceCircleImage(mContext, mGirlsAdBean?.bellead_bellepic ?: "", mIvGirlBottomAdUserFace)
                mTvGirlBottomAdUserNickName.text = mGirlsAdBean?.bellead_bellename ?: ""
                mRlGirlBottomAdContainer.setOnClickListener {  }
            }
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return GridLayoutManager(mContext, 2)
    }

    override fun getItemDecoration(): RecyclerView.ItemDecoration {
        val itemDecoration = super.getItemDecoration() as DividerItemDecoration
        itemDecoration.setDrawable(ColorDrawable(Color.TRANSPARENT))
        return itemDecoration
    }

}