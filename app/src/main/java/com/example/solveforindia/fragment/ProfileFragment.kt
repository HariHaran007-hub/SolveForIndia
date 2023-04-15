package com.zero.golgol.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.zero.golgol.activity.ChallengeActivity
import com.zero.golgol.activity.LeaderboardActivity
import com.zero.golgol.adapter.LeaderboardAdapter
import com.zero.golgol.databinding.FragmentProfileBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.ActiveChallenges
import com.zero.golgol.model.Leaderboard
import com.zero.golgol.model.UserDetails
import com.zero.golgol.utils.Utils

class ProfileFragment : BaseFragment(), ClickListener {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LeaderboardAdapter
    private lateinit var leaderboardList: MutableList<Leaderboard>

    private lateinit var golPalsId : MutableList<String>

    private lateinit var  databaseReference : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance()
            .getReference("${Utils.User}/${FirebaseAuth.getInstance().currentUser!!.uid}")

        initViews()
        loadLeaderboard()
        loadActiveChallenge()
        loadProfile()
        loadGoalPalsDp()
        clickListener()
    }

    private fun initViews() {
        binding.tvName.text = user.username
        binding.myGols.text = user.gols.toString()
        binding.tvBadges.text = user.badges.toString()
    }

    override fun onStart() {
        super.onStart()
        setUpRecyclerView()
    }

    private fun clickListener() {
        binding.tvShowChallenges.setOnClickListener { startActivity(Intent(requireActivity(), ChallengeActivity::class.java)) }

        binding.tvShowLeaderboard.setOnClickListener { startActivity(Intent(requireActivity(), LeaderboardActivity::class.java)) }
    }

    private fun loadActiveChallenge(){
        val activeChallengeList = mutableListOf<ActiveChallenges>()
        databaseReference.child(Utils.ActiveChallenges).get().addOnSuccessListener { snapshot->
            if(snapshot.exists()){

                binding.lnrActiveChallengeHolder.visibility = View.VISIBLE
                binding.tvNoChallengeExist.visibility = View.GONE

                for(c in snapshot.children){
                        val activeChallenge = c.getValue(ActiveChallenges::class.java)
                        activeChallengeList.add(activeChallenge!!)
                }
                activeChallengeList.sortByDescending {
                    it.progress
                }

                showActiveChallenge(activeChallengeList[0])

            }else{
                binding.lnrActiveChallengeHolder.visibility = View.GONE
                binding.tvNoChallengeExist.visibility = View.VISIBLE
            }
        }
    }

    private fun showActiveChallenge(activeChallenge: ActiveChallenges) {
        Glide.with(requireActivity())
            .asDrawable()
            .load(activeChallenge.challengeLogo)
            .into(binding.ivChallengeLogo)
        binding.tvChallengeTitle.text = activeChallenge.challengeTitle
        binding.tvChallengeDescription.text = activeChallenge.challengeDescription
        binding.progressChallenge.progress = activeChallenge.progress
    }

    private fun loadLeaderboard() {
        leaderboardList = mutableListOf()
        val leaderBoardTempList = mutableListOf<UserDetails>()
        val leaderboardReference = FirebaseDatabase.getInstance().getReference(Utils.User)

        leaderboardReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
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
                        if(i != 3){
                            val leaderSort = Leaderboard(i, it.avatar!!,
                                it.userName!!, it.gols!!, it.userId!!)
                            leaderboardList.add(leaderSort)
                        }
                    }
                    adapter.updateItems(leaderboardList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
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

    private fun setUpRecyclerView(){
        adapter = LeaderboardAdapter(requireContext(), leaderboardList, this)
        binding.rvLeaderboard.setHasFixedSize(true)
        binding.rvLeaderboard.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLeaderboard.adapter = adapter

        val lm = binding.rvLeaderboard.layoutManager
        binding.rvLeaderboard.layoutManager =
            object : LinearLayoutManager(requireContext()){ override fun canScrollVertically(): Boolean { return false } }
        lm?.scrollToPosition(3)
    }

    private fun loadProfile(){
        databaseReference.get().addOnSuccessListener {snapshot->
            if(snapshot.exists()){
                val user = snapshot.getValue(UserDetails::class.java)
                Log.d("Main", "loadProfile: ${user!!.userName}")
                Glide.with(requireActivity())
                    .asDrawable()
                    .load(user.avatar)
                    .into(binding.ivMyImage)
                binding.myGols.text = user.gols.toString()
                binding.tvName.text = user.userName
                binding.tvBadges.text = user.badges.toString()
            }
        }
    }

    private fun loadGoalPalsDp() {

        golPalsId = mutableListOf()

        FirebaseDatabase.getInstance()
            .getReference("${Utils.User}/${FirebaseAuth.getInstance().currentUser!!.uid}/golPals")
            .get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val golPals = snapshot.value as HashMap<*, *>

                    for (c in golPals.values) {
                        golPalsId.add(c.toString())
                    }
                    var i = 0
                    if (!golPals.values.isEmpty()) {
                        binding.tvGolPals.visibility = View.VISIBLE
                        binding.tvNogolpal.visibility = View.GONE

                        for (c in golPalsId) {
                            i++

                            if (i == 1) {
                                binding.ivUserImage.visibility = View.VISIBLE
                                FirebaseDatabase.getInstance()
                                    .getReference("${Utils.User}/${c}/avatar").get()
                                    .addOnSuccessListener {
                                        val avatar = it.value as Long
                                        Glide.with(requireActivity())
                                            .asDrawable()
                                            .load(avatar.toInt())
                                            .into(binding.ivUserImage)
                                    }
                            }

                            if (i == 2) {
                                binding.ivUserImage1.visibility = View.VISIBLE

                                FirebaseDatabase.getInstance()
                                    .getReference("${Utils.User}/${c}/avatar").get()
                                    .addOnSuccessListener {
                                        val avatar = it.value as Long
                                        Glide.with(requireActivity())
                                            .asDrawable()
                                            .load(avatar.toInt())
                                            .into(binding.ivUserImage1)
                                    }
                            }

                            if (i == 3) {
                                binding.ivUserImage2.visibility = View.VISIBLE

                                FirebaseDatabase.getInstance()
                                    .getReference("${Utils.User}/${c}/avatar").get()
                                    .addOnSuccessListener {
                                        val avatar = it.value as Long
                                        Glide.with(requireActivity())
                                            .asDrawable()
                                            .load(avatar.toInt())
                                            .into(binding.ivUserImage2)
                                    }
                            }

                            if (i == 4) {
                                binding.ivUserImage3.visibility = View.VISIBLE

                                FirebaseDatabase.getInstance()
                                    .getReference("${Utils.User}/${c}/avatar").get()
                                    .addOnSuccessListener {
                                        val avatar = it.value as Long
                                        Glide.with(requireActivity())
                                            .asDrawable()
                                            .load(avatar.toInt())
                                            .into(binding.ivUserImage3)
                                    }
                            }

                            if (i == 5) {
                                binding.ivUserImage4.visibility = View.VISIBLE

                                FirebaseDatabase.getInstance()
                                    .getReference("${Utils.User}/${c}/avatar").get()
                                    .addOnSuccessListener {
                                        val avatar = it.value as Long
                                        Glide.with(requireActivity())
                                            .asDrawable()
                                            .load(avatar.toInt())
                                            .into(binding.ivUserImage4)
                                    }
                            }

                            if (i == 6) {
                                binding.ivUserImage5.visibility = View.VISIBLE
                                FirebaseDatabase.getInstance()
                                    .getReference("${Utils.User}/${c}/avatar").get()
                                    .addOnSuccessListener {
                                        val avatar = it.value as Long
                                        Glide.with(requireActivity())
                                            .asDrawable()
                                            .load(avatar.toInt())
                                            .into(binding.ivUserImage5)
                                    }
                            } else {
                                binding.tvMoreCount.visibility = View.VISIBLE
                                binding.tvMoreCount.text = (golPalsId.size - 4).toString()
                            }
                        }
                    }
                }

            }
    }
}