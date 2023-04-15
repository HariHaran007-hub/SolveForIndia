package com.zero.golgol.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zero.golgol.databinding.ItemChallengeInactiveBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.InActiveChallenge

class InactiveChallengeAdapter(private val context: Context, private var challengeList: MutableList<InActiveChallenge>,
                               private val listener: ClickListener): RecyclerView.Adapter<InactiveChallengeAdapter.ViewHolder>() {

    private var _binding: ItemChallengeInactiveBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemChallengeInactiveBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val challenge = challengeList[position]

        Glide.with(context)
            .asDrawable()
            .load(challenge.challengeLogo)
            .into(binding.ivChallengeLogo)

        binding.tvChallengeTitle.text = challenge.challengeTitle
        binding.tvChallengeDescription.text = challenge.challengeDescription
        binding.btnActivateChallenge.setOnClickListener { listener.buttonClick(position) }
    }

    fun updateItems(challengeList: MutableList<InActiveChallenge>){
        this.challengeList = challengeList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return challengeList.size
    }
}