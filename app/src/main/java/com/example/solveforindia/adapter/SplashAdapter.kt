package com.zero.golgol.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zero.golgol.R
import com.zero.golgol.databinding.ItemSplashBinding
import com.zero.golgol.listener.ClickListener

class SplashAdapter(private val context: Context, private val listener: ClickListener): RecyclerView.Adapter<SplashAdapter.ViewHolder>() {

    private var _binding: ItemSplashBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val imageList: List<Int> = listOf(R.drawable.img_splash_1, R.drawable.img_splash_2, R.drawable.img_splash_3)
    private val headingList: List<String> = listOf(context.getString(R.string.find_others_n_who_are_playing_near_you_n_and_the_game_you_like),
        context.getString(R.string.welcome_2),
        context.getString(R.string.welcome_3))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemSplashBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        binding.ivSplashBackground.setImageResource(imageList[position])
        binding.tvHeading.text = headingList[position]
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

}