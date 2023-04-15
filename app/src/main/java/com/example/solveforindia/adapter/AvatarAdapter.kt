package com.zero.golgol.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zero.golgol.R
import com.zero.golgol.listener.ClickListener

class AvatarAdapter(private val context: Context, private val list: MutableList<Int>, private val listener: ClickListener): RecyclerView.Adapter<AvatarAdapter.ViewHolder>() {

    private var isAvatarSelected = false

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val avatar: ImageView = itemView.findViewById(R.id.ivAvatar)
    }

    private val colorsList = mutableListOf(R.drawable.bg_avatar_1, R.drawable.bg_avatar_2, R.drawable.bg_avatar_3, R.drawable.bg_avatar_4,
        R.drawable.bg_avatar_5, R.drawable.bg_avatar_6, R.drawable.bg_avatar_7, R.drawable.bg_avatar_8,
        R.drawable.bg_avatar_9, R.drawable.bg_avatar_10,
        R.drawable.bg_avatar_11, R.drawable.bg_avatar_12)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_avatar, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context)
            .asDrawable()
            .load(list[position])
            .into(holder.avatar)

        holder.avatar.setOnClickListener {
            if (!isAvatarSelected) {
                isAvatarSelected = true
                holder.avatar.background =
                    ResourcesCompat.getDrawable(context.resources, colorsList[position], null)
                holder.avatar.isEnabled = false
                holder.avatar.isClickable = false
                listener.buttonClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}