package com.cmdv.common.listeners

import androidx.viewpager.widget.ViewPager

abstract class SimplePageChangeListener : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }
    override fun onPageSelected(position: Int) { }
    override fun onPageScrollStateChanged(state: Int) { }
}