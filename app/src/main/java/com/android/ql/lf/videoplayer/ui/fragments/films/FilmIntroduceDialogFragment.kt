package com.android.ql.lf.videoplayer.ui.fragments.films

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.ql.lf.videoplayer.R

class FilmIntroduceDialogFragment : BottomSheetDialogFragment() {


    private var contentView: View? = null
    private var title:String? = null
    private var crumbs:String? = null
    private var des:String? = null
    private var introduce:String? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        contentView = View.inflate(context, R.layout.dialog_player_info_introduce_layout, null)
        contentView?.findViewById<TextView>(R.id.mTvPlayerInfoIntroduceTitle)?.setOnClickListener{ dismiss() }
        contentView?.findViewById<TextView>(R.id.mTvPlayerInfoIntroduceTitle)?.text = title
        contentView?.findViewById<TextView>(R.id.mTvPlayerInfoIntroduceCrumbs)?.text = crumbs
        contentView?.findViewById<TextView>(R.id.mTvPlayerInfoIntroduceDes)?.text = des
        contentView?.findViewById<TextView>(R.id.mTvPlayerInfoIntroduceIntroduce)?.text = introduce
        dialog.setContentView(contentView)
        return dialog
    }

    fun setData(title:String?,crumbs:String?,des:String?,introduce:String?){
        this.title = title
        this.crumbs = crumbs
        this.des = des
        this.introduce = introduce
    }

}