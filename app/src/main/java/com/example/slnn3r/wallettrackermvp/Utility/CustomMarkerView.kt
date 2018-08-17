package com.example.slnn3r.wallettrackermvp.Utility

import android.content.Context
import android.widget.TextView
import com.example.slnn3r.wallettrackermvp.R
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF


class CustomMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val tvContent: TextView = findViewById(R.id.tvContent)
    private var mOffset: MPPointF? = null

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    override fun refreshContent(e: Entry?, highlight: Highlight?) {

        tvContent.text = context.getString(R.string.formatDisplay2DecimalMoney, e!!.y)

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