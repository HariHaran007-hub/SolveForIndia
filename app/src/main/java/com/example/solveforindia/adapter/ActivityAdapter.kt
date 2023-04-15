package com.zero.golgol.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zero.golgol.databinding.ItemActivityBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Activity

class ActivityAdapter(private val context: Context, private val activitiesList: MutableList<Activity>,
                      private val listener: ClickListener): RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    private var _binding: ItemActivityBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemActivityBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activitiesList[position]

        Glide.with(context)
            .asDrawable()
            .load(activity.activityImage)
            .into(binding.ivActivity)
        binding.tvActivityName.text = activity.activityName

        if (activity.isFavorite){
            binding.ivActivity.borderWidth = 10F
            binding.ivFavorite.visibility = View.VISIBLE
        }
        else{
            binding.ivActivity.borderWidth = 0F
            binding.ivFavorite.visibility = View.INVISIBLE
        }

        binding.root.setOnClickListener {
            listener.buttonClick(position, "Activity")
        }
    }

    override fun getItemCount(): Int {
        return activitiesList.size
    }

}