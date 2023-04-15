package com.zero.golgol.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zero.golgol.databinding.ItemRewardBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Reward

class RewardAdapter(private val context: Context, private var rewardsList: MutableList<Reward>,
                    private val listener: ClickListener): RecyclerView.Adapter<RewardAdapter.ViewHolder>() {

    private var _binding: ItemRewardBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemRewardBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val challenge = rewardsList[position]

        Glide.with(context)
            .asDrawable()
            .load(challenge.rewardLogo)
            .into(binding.ivRewardLogo)

        binding.tvReward.text = challenge.rewardTitle
        binding.tvRewardProgress.text = challenge.rewardProgress.toString() + "%"
        binding.progressReward.visibility = View.VISIBLE

        binding.progressReward.progress = challenge.rewardProgress
    }

    fun updateItems(rewardsList: MutableList<Reward>){
        this.rewardsList = rewardsList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return rewardsList.size
    }

}