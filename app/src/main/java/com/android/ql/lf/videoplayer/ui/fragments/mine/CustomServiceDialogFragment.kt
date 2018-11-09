package com.android.ql.lf.videoplayer.ui.fragments.mine

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.android.ql.lf.videoplayer.R
import kotlinx.android.synthetic.main.dialog_custom_service_layout.*

class CustomServiceDialogFragment : DialogFragment() {

    private var textView: TextView? = null

    private var content:String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val contentView = inflater.inflate(R.layout.dialog_custom_service_layout, container, false)
        textView = contentView.findViewById(R.id.mTvCustomServiceContent)
        textView?.text = content
        return contentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.window.setBackgroundDrawable(ColorDrawable())
        mBtCustomServiceDialog.setOnClickListener { dismiss() }
    }


    fun setData(content: String) {
        this.content = content
    }

}