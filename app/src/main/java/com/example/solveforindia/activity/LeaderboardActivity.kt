package com.zero.golgol.activity

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.FirebaseDatabase

import com.zero.golgol.adapter.LeaderboardAdapter
import com.zero.golgol.databinding.ActivityLeaderboardBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Leaderboard
import com.zero.golgol.model.UserDetails
import com.zero.golgol.utils.Utils

class LeaderboardActivity : BaseActivity(), ClickListener {

    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var adapter: LeaderboardAdapter

    private lateinit var leaderboardList: MutableList<Leaderboard>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        init()
        loadLeaderboard()
    }

    private fun init(){
        binding.ivBack.setOnClickListener { finish() }
        setUpRecyclerViews()
    }

    private fun loadLeaderboard() {
        val leaderBoardTempList = mutableListOf<UserDetails>()
        binding.layoutProgress.progressBarHolder.visibility = View.VISIBLE
        val databaseReference = FirebaseDatabase.getInstance().getReference(Utils.User)
        databaseReference.get().addOnSuccessListener { snapshot->
            if(snapshot.exists()){
                for(c in snapshot.children){
                    val user = c.getValue(UserDetails::class.java)
                    leaderBoardTempList.add(user!!)
                }
                leaderBoardTempList.sortByDescending {
                    it.gols
                }
                var i = 0
                leaderBoardTempList.forEach {
                    i++
                   val leaderSort = Leaderboard(i, it.avatar!!,
                       it.userName!!, it.gols!!, it.userId!!
                   )
                    leaderboardList.add(leaderSort)
                }
                adapter.updateItems(leaderboardList)
                binding.layoutProgress.progressBarHolder.visibility = View.INVISIBLE
            }
        }
    }

    private fun setUpRecyclerViews(){
        leaderboardList = mutableListOf()
        adapter = LeaderboardAdapter(this, leaderboardList, this)
        binding.rvLeaderboard.setHasFixedSize(true)
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(this)
        binding.rvLeaderboard.adapter = adapter
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