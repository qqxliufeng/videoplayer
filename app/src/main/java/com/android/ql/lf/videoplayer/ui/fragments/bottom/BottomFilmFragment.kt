package com.android.ql.lf.videoplayer.ui.fragments.bottom

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.View
import android.view.ViewGroup
import com.android.ql.lf.baselibaray.component.ApiParams
import com.android.ql.lf.baselibaray.ui.fragment.BaseNetWorkingFragment
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.ui.fragments.films.FilmItemFragment
import com.android.ql.lf.videoplayer.utils.VIDEO_MODULE
import com.android.ql.lf.videoplayer.utils.VIDEO_NAV_ACT
import com.android.ql.lf.videoplayer.utils.getBaseParamsWithModAndAct
import com.android.ql.lf.videoplayer.utils.toArrayList
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_bottom_film_layout.*
import org.json.JSONArray
import org.json.JSONObject

class BottomFilmFragment : BaseNetWorkingFragment() {

    val titles = arrayListOf<FilmMenuBean>()

    var json = ""

    override fun getLayoutId() = R.layout.fragment_bottom_film_layout

    override fun initView(view: View?) {
        (mTlFilmNavigation.layoutParams as ViewGroup.MarginLayoutParams).topMargin = statusBarHeight
        mVpFilmViewPager.adapter = FilmViewPagerAdapter(childFragmentManager)
        mVpFilmViewPager.offscreenPageLimit = 3
        mTlFilmNavigation.setupWithViewPager(mVpFilmViewPager)
        mPresent.getDataByPost(0x0, getBaseParamsWithModAndAct(VIDEO_MODULE,VIDEO_NAV_ACT))
    }

    override fun onRequestStart(requestID: Int) {
        super.onRequestStart(requestID)
        getFastProgressDialog("正在加载……")
    }

    override fun onHandleSuccess(requestID: Int, obj: Any?) {
        if (obj!=null) {
            ((obj as JSONObject).optJSONArray("classify") as JSONArray).let { json ->
                val tempList = json.toArrayList<FilmMenuBean>()
                titles.addAll(tempList ?: arrayListOf())
            }
            obj.toString().apply { json = this }
            mVpFilmViewPager.adapter?.notifyDataSetChanged()
        }
    }

    inner class FilmViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

        override fun getItem(position: Int) = FilmItemFragment.newInstance(json,titles[position].classify_id,titles[position].classify_type)

        override fun getCount() = titles.size

        override fun getPageTitle(position: Int): CharSequence? = titles[position].classify_name
    }
}

data class FilmMenuBean(val classify_id: Int, val classify_name: String, val classify_is_show: String, val classify_times: String,val classify_type:Int)

