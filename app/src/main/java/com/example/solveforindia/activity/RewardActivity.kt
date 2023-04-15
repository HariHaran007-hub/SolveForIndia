package com.zero.golgol.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase
import com.zero.golgol.adapter.RewardAdapter
import com.zero.golgol.databinding.ActivityRewardBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Reward
import com.zero.golgol.utils.Utils

class RewardActivity : BaseActivity(), ClickListener {

    private lateinit var binding: ActivityRewardBinding
    private lateinit var adapter: RewardAdapter

    private lateinit var rewardsList: MutableList<Reward>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        clickListeners()
        setupViews()
        loadRewards()
    }

    private fun setupViews() {
        rewardsList = mutableListOf()
        adapter = RewardAdapter(this, rewardsList, this)
        binding.rvRewards.setHasFixedSize(true)
        binding.rvRewards.layoutManager = LinearLayoutManager(this)
        binding.rvRewards.adapter = adapter
    }


    private fun clickListeners() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun loadRewards() {
        binding.layoutProgress.progressBarHolder.visibility = View.VISIBLE

        FirebaseDatabase.getInstance().getReference(Utils.Reward).get().addOnSuccessListener { snapshot->
            if(snapshot.exists()){
                rewardsList.clear()
                for(c in snapshot.children){
                    val rewards = c.getValue(Reward::class.java)
                    rewardsList.add(rewards!!)
                }
                adapter.updateItems(rewardsList)
                binding.layoutProgress.progressBarHolder.visibility = View.INVISIBLE
            }
        }
    }

    override fun buttonClick() {
        //
    }

    override fun buttonClick(position: Int, type: String) {
        //
    }

    override fun buttonClick(position: Int) {
        //
    }
}