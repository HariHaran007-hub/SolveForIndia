package com.zero.golgol.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.zero.golgol.R
import com.zero.golgol.databinding.ItemLeaderboardBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Leaderboard


class LeaderboardAdapter(private val context: Context, private var leaderboard: MutableList<Leaderboard>,
                         private val listener: ClickListener): RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    private var _binding: ItemLeaderboardBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemLeaderboardBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val score = leaderboard[position]

        if (position == 0){
            binding.ivTrophy.visibility = View.VISIBLE
        }
        else{
            binding.ivTrophy.visibility = View.INVISIBLE
        }

        Glide.with(context)
            .asDrawable()
            .load(score.userImage)
            .into(binding.ivUserImage)

        binding.tvSno.text = score.sNo.toString()
        binding.tvName.text = score.userName
        binding.tvGols.text = score.gols.toString()

        if (position % 2 == 0){
            binding.lnrRoot.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.greyTranslucent, null))
        }

        if (score.userId == FirebaseAuth.getInstance().uid.toString()){ //Todo: Change with user id from api response
            binding.lnrRoot.setBackgroundColor(ResourcesCompat.getColor(context.resources, R.color.orangeLight, null))
            binding.tvSno.setTextColor(Color.WHITE)
            binding.tvName.setTextColor(Color.WHITE)
            binding.tvGols.setTextColor(Color.WHITE)

            binding.tvName.text = "You"
            binding.tvGols.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_gols_small_white, 0, 0, 0);
        }
    }

    override fun getItemCount(): Int {
        return leaderboard.size
    }

    fun updateItems(leaderBoardList : MutableList<Leaderboard>){
        this.leaderboard = leaderBoardList
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = position

}