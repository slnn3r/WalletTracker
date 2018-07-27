package com.example.slnn3r.wallettrackermvp.Utility

import android.content.Context
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.widget.TextView
import com.example.slnn3r.wallettrackermvp.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val tvContent: TextView

    private var mOffset: MPPointF? = null

    init {

        // find your layout components
        tvContent = findViewById(R.id.tvContent)
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        tvContent.text = "$ " + e!!.y

        // this will perform necessary layouting
        super.refreshContent(e, highlight)
    }

    override fun getOffset(): MPPointF {

        if (mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = MPPointF((-(width / 2)).toFloat(), (-height).toFloat())
        }

        return mOffset as MPPointF
    }
}