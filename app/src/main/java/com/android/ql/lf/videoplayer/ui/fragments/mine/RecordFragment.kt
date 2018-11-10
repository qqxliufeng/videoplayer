package com.android.ql.lf.videoplayer.ui.fragments.mine

import android.content.Context
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.android.ql.lf.baselibaray.ui.fragment.BaseRecyclerViewFragment
import com.android.ql.lf.baselibaray.utils.GlideManager
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.data.film.RecordBean
import com.android.ql.lf.videoplayer.ui.activities.PlayerActivity
import com.android.ql.lf.videoplayer.utils.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import kotlinx.android.synthetic.main.fragment_record_layout.*
import org.jetbrains.anko.support.v4.toast
import java.lang.StringBuilder

class RecordFragment : BaseRecyclerViewFragment<RecordBean>() {

    private var isEditMode = false

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        setHasOptionsMenu(true)
    }

    override fun getLayoutId() = R.layout.fragment_record_layout

    override fun createAdapter(): BaseQuickAdapter<RecordBean, BaseViewHolder> =
        object : BaseQuickAdapter<RecordBean, BaseViewHolder>(R.layout.adapter_record_item_layout, mArrayList) {
            override fun convert(helper: BaseViewHolder?, item: RecordBean?) {
                helper?.setGone(R.id.mCtvRecordItemSelect, isEditMode)
                if (isEditMode) {
                    helper?.setChecked(R.id.mCtvRecordItemSelect, item!!.isEditMode)
                }
                GlideManager.loadImage(mContext, item?.collected_pic, helper?.getView(R.id.mIvRecordItemPic))
                helper?.setText(R.id.mTvRecordItemName, item?.collected_name)
            }
        }

    override fun onRefresh() {
        super.onRefresh()
        mPresent.getDataByPost(0x0, getBaseParamsWithPage(USER_MODULE, MY_RECORDED_ACT, currentPage))
    }

    override fun onLoadMore() {
        super.onLoadMore()
        mPresent.getDataByPost(0x0, getBaseParamsWithPage(USER_MODULE, MY_RECORDED_ACT, currentPage))
    }

    override fun initView(view: View?) {
        super.initView(view)
        mLlRecordBottomContainer.visibility = View.GONE
        mTvRecordBottomAllSelect.setOnClickListener { view1 ->
            if (mArrayList.isEmpty()) {
                return@setOnClickListener
            }
            mArrayList.forEach { it.isEditMode = true }
            mBaseAdapter.notifyDataSetChanged()
            mTvRecordBottomDelete.text = "删除(${mArrayList.size})"
        }
        mTvRecordBottomDelete.setOnClickListener { view2 ->
            if (mArrayList.none { it.isEditMode }) {
                return@setOnClickListener
            }
            val stringBuilder = StringBuilder()
            mArrayList.filter { it.isEditMode }.forEach {
                stringBuilder.append(it.recorded_id).append(",")
            }
            mPresent.getDataByPost(
                0x1,
                getBaseParamsWithModAndAct(USER_MODULE, DEL_RECORDED_ACT).addParam("rid", stringBuilder.toString())
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.record_menu, menu)
    }

    override fun getEmptyMessage(): String {
        return "暂无观看记录"
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (mArrayList.isEmpty()) {
            return super.onOptionsItemSelected(item)
        }
        if (isEditMode) {
            item?.title = "编辑"
            mLlRecordBottomContainer.visibility = View.GONE
        } else {
            item?.title = "取消"
            mArrayList.forEach { it.isEditMode = false }
            mLlRecordBottomContainer.visibility = View.VISIBLE
            mTvRecordBottomDelete.text = "删除(0)"
        }
        isEditMode = !isEditMode
        mBaseAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        if (requestID == 0x1) {
            getFastProgressDialog("正在删除……")
        }
    }

    override fun <T : Any?> onRequestSuccess(requestID: Int, result: T) {
        if (requestID == 0x0) {
            processList(result as String, RecordBean::class.java)
        } else if (requestID == 0x1) {
            val check = checkResultCode(result)
            if (check?.code == SUCCESS_CODE) {
                toast("删除成功")
                val tempList = mArrayList.filter { !it.isEditMode }
                mArrayList.clear()
                mArrayList.addAll(tempList)
                mBaseAdapter.notifyDataSetChanged()
            } else {
                toast("删除失败")
            }
        }
    }

    override fun onMyItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        super.onMyItemClick(adapter, view, position)
        if (isEditMode) {
            mArrayList[position].isEditMode = !mArrayList[position].isEditMode
            mBaseAdapter.notifyItemChanged(position)
            mTvRecordBottomDelete.text = "删除(${mArrayList.filter { it.isEditMode }.size})"
        } else {
            PlayerActivity.startPlayerActivity(mContext, mArrayList[position].recorded_theme ?: 0)
        }
    }
}