package com.android.ql.lf.videoplayer.ui.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v7.app.AppCompatActivity
import android.support.v7.content.res.AppCompatResources
import android.widget.ImageView
import com.android.ql.lf.videoplayer.R
import com.android.ql.lf.videoplayer.ui.fragments.bottom.BottomShowFragment
import kotlinx.android.synthetic.main.test_activity_tab_layout.*

class TestTabActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_activity_tab_layout)
        val firstTab = mTlTest.newTab()
        firstTab.icon = resources.getDrawable(R.drawable.bottom_film)
        firstTab.text = "电影"
        firstTab.tag = 0
        val secondTab = mTlTest.newTab()
        secondTab.icon = resources.getDrawable(R.drawable.bottom_film)
        secondTab.text = "电影"
        secondTab.tag = 1
        val thirthTab = mTlTest.newTab()
        val imageView = ImageView(this)
        imageView.setImageResource(R.drawable.bottom_girl)
        thirthTab.customView = imageView
        thirthTab.tag = 2
        val fourthTab = mTlTest.newTab()
        fourthTab.icon = resources.getDrawable(R.drawable.bottom_film)
        fourthTab.text = "电影"
        fourthTab.tag = 3
        val fifthTab = mTlTest.newTab()
        fifthTab.icon = resources.getDrawable(R.drawable.bottom_film)
        fifthTab.text = "电影"
        fifthTab.tag = 4
        mTlTest.addTab(firstTab)
        mTlTest.addTab(secondTab)
        mTlTest.addTab(thirthTab)
        mTlTest.addTab(fourthTab)
        mTlTest.addTab(fifthTab)
        firstTab.select()
        mVpTest.adapter = object : FragmentStatePagerAdapter(supportFragmentManager){

            override fun getCount() = 4

            override fun getItem(position: Int) = BottomShowFragment()

        }

        mTlTest.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.tag == 2){
                    if (mVpTest.currentItem > 1){
                        mTlTest.getTabAt(mVpTest.currentItem + 1)?.select()
                    }else{
                        mTlTest.getTabAt(mVpTest.currentItem)?.select()
                    }
                    return
                }
                var index = tab?.position!!
                if (index > 2){
                    index = tab.position - 1
                }
                mVpTest.currentItem = index
            }
        })
    }
}