package com.android.ql.lf.videoplayer.ui.activities

import android.graphics.Color
import android.os.Build
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.util.Log
import android.view.View
import com.android.ql.lf.baselibaray.ui.activity.BaseActivity
import com.android.ql.lf.baselibaray.utils.BottomNavigationViewHelper
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.ui.fragments.bottom.*
import kotlinx.android.synthetic.main.activity_main_layout.*

class MainActivity : BaseActivity() {


    override fun getLayoutId() = R.layout.activity_main_layout

    override fun initView() {
        statusBarColor = Color.parseColor("#f4f5f6")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        BottomNavigationViewHelper.disableShiftMode(mMainNavigation)
        mMainNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.mMainBottomFilm->{
                    mMainViewPager.currentItem = 0
                }
                R.id.mMainBottomGirls->{
                    mMainViewPager.currentItem = 1
                }
                R.id.mMainBottomVip->{
                    mMainViewPager.currentItem = 2
                }
                R.id.mMainBottomShow->{
                    mMainViewPager.currentItem = 3
                }
                R.id.mMainBottomMine->{
                    mMainViewPager.currentItem = 4
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
        mMainViewPager.adapter = BottomNavigationViewPagerAdapter(supportFragmentManager)
        mMainViewPager.offscreenPageLimit = 5

        Log.e("TAG",cacheDir.absolutePath)
    }
}

class BottomNavigationViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager){

    override fun getItem(position: Int) = when (position){
        0->{
            BottomFilmFragment()
        }

        1->{
            BottomGirlsFragment()
        }

        2->{
            BottomVipFragment()
        }

        3->{
            BottomShowFragment()
        }

        4->{
            BottomMineFragment()
        }
        else->{
            null
        }

    }


    override fun getCount() = 5

}


