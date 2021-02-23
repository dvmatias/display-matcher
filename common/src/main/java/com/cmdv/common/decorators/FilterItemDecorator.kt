package com.cmdv.common.decorators

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cmdv.common.R


class FilterItemDecorator(private val context: Context) : RecyclerView.ItemDecoration() {

    private var dividerTop: Drawable? = null
    private var dividerMiddle: Drawable? = null
    private var dividerBottom: Drawable? = null

    init {
        dividerTop = ContextCompat.getDrawable(context, R.drawable.shape_item_filter_bottom_sheet_top)
        dividerMiddle = ContextCompat.getDrawable(context, R.drawable.shape_item_filter_bottom_sheet_middle)
        dividerBottom = ContextCompat.getDrawable(context, R.drawable.shape_item_filter_bottom_sheet_bottom)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val divider: Drawable? =
                when (i) {
                    0 -> dividerTop
                    (childCount - 1) -> dividerBottom
                    else -> dividerMiddle
                }
            val child = parent.getChildAt(i)
            divider?.setBounds(child.left, child.top, child.right, child.bottom + 4)
            divider?.draw(c)
        }
    }
}