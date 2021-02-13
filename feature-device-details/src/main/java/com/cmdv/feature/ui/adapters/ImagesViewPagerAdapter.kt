package com.cmdv.feature.ui.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.cmdv.feature.databinding.ItemImageBinding

class ImagesViewPagerAdapter(private val context: Context, pager: ViewPager2) : RecyclerView.Adapter<ImagesViewPagerAdapter.ImageViewHolder>() {
    private lateinit var binding: ItemImageBinding
    private var items: ArrayList<String> = arrayListOf()

    fun setItems(images: ArrayList<String>) {
        items.apply {
            clear()
            items.addAll(images)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(context, items[position])
    }

    override fun getItemCount(): Int = items.size

    class ImageViewHolder(private val binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, imageUrl: String) {
            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeCap = Paint.Cap.ROUND
            circularProgressDrawable.strokeWidth = 7f
            circularProgressDrawable.centerRadius = 36f
            circularProgressDrawable.start()
            Glide.with(context)
                .load(imageUrl)
                .placeholder(circularProgressDrawable)
                .into(binding.imageView)
        }

    }

}