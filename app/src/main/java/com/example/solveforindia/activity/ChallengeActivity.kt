package com.zero.golgol.activity


import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.zero.golgol.R
import com.zero.golgol.adapter.ActiveChallengeAdapter
import com.zero.golgol.adapter.InactiveChallengeAdapter
import com.zero.golgol.databinding.ActivityChallengeBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.ActiveChallenges
import com.zero.golgol.model.InActiveChallenge
import com.zero.golgol.utils.Utils

class ChallengeActivity : BaseActivity(), ClickListener {

    private lateinit var binding: ActivityChallengeBinding

    private lateinit var adapterActive: ActiveChallengeAdapter
    private lateinit var adapterInactive: InactiveChallengeAdapter

    private lateinit var activeChallengeList: MutableList<com.zero.golgol.model.Challenge>
    private lateinit var inactiveChallengeList: MutableList<InActiveChallenge>
    private lateinit var activeChallengesListForCompare: MutableList<ActiveChallenges>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChallengeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        init()
        clickListeners()
    }

    private fun init(){
        binding.btnActiveChallenges.isActivated = true
        binding.rvActiveChallenges.visibility = View.VISIBLE
        setupRecyclerViews()
        loadInactiveChallenges()
        loadActiveChallenges()
    }

    private fun setupRecyclerViews() {
        activeChallengeList = mutableListOf()
        adapterActive = ActiveChallengeAdapter(this, activeChallengeList, this)
        binding.rvActiveChallenges.setHasFixedSize(true)
        binding.rvActiveChallenges.layoutManager = LinearLayoutManager(this)
        binding.rvActiveChallenges.adapter = adapterActive

        inactiveChallengeList = mutableListOf()
        adapterInactive = InactiveChallengeAdapter(applicationContext, inactiveChallengeList, this@ChallengeActivity)
        binding.rvInactiveChallenges.setHasFixedSize(true)
        binding.rvInactiveChallenges.layoutManager = LinearLayoutManager(this@ChallengeActivity)
        binding.rvInactiveChallenges.adapter = adapterInactive
    }

    private fun loadActiveChallenges() {
        activeChallengesListForCompare = mutableListOf()
        binding.layoutProgress.progressBarHolder.visibility = View.VISIBLE

        val database : DatabaseReference =
            FirebaseDatabase.getInstance()
                .getReference("${Utils.User}/${FirebaseAuth.getInstance().uid!!}/${Utils.ActiveChallenges}")

        database.get().addOnSuccessListener { snapshot->
                if(snapshot.exists()){
                    activeChallengeList.clear()
                    for(c in snapshot.children){
                        val challenges = c.getValue(ActiveChallenges::class.java)
                        activeChallengesListForCompare.add(challenges!!)
                        val ch = com.zero.golgol.model.Challenge(challenges!!.challengeLogo,challenges.challengeTitle
                            ,challenges.challengeDescription,challenges.progress)
                            activeChallengeList.add(ch)
                    }
                    adapterActive.updateItems(activeChallengeList)
                    binding.layoutProgress.progressBarHolder.visibility = View.INVISIBLE

                    if (activeChallengeList.isEmpty()) binding.tvNoChallenges.visibility = View.VISIBLE else binding.tvNoChallenges.visibility = View.GONE
                }
                else binding.tvNoChallenges.visibility = View.VISIBLE
        }
    }

    private fun loadInactiveChallenges() {
        binding.layoutProgress.progressBarHolder.visibility = View.VISIBLE

        val challengesReference :DatabaseReference = FirebaseDatabase.getInstance().getReference(Utils.Challenges)

        challengesReference.get().addOnSuccessListener { snapshot->
            if(snapshot.exists()){
                inactiveChallengeList.clear()
                for (c in snapshot.children){
                    val challenge = c.getValue(InActiveChallenge::class.java)
                    inactiveChallengeList.add(challenge!!)
                }
                adapterInactive.updateItems(inactiveChallengeList)
                binding.layoutProgress.progressBarHolder.visibility = View.INVISIBLE
            }
        }
    }

    private fun clickListeners(){
        binding.ivBack.setOnClickListener { finish() }

        binding.btnActiveChallenges.setOnClickListener {
            showSelectedTab(Challenge.ACTIVE)
        }

        binding.btnInactiveChallenges.setOnClickListener {
            showSelectedTab(Challenge.INACTIVE)
        }
    }

    private fun showSelectedTab(mode: Challenge) {
        binding.tvNoChallenges.visibility = View.GONE
        binding.btnActiveChallenges.isActivated = false
        binding.btnInactiveChallenges.isActivated = false
        binding.btnActiveChallenges.setTextColor(ResourcesCompat.getColor(resources, R.color.blackTranslucent, null))
        binding.btnInactiveChallenges.setTextColor(ResourcesCompat.getColor(resources, R.color.blackTranslucent, null))

        when(mode){
            Challenge.ACTIVE -> {
                binding.btnActiveChallenges.isActivated = true
                binding.btnActiveChallenges.setTextColor(Color.BLACK)

                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
                binding.rvInactiveChallenges.visibility = View.INVISIBLE
                binding.rvActiveChallenges.visibility = View.VISIBLE
                if (activeChallengeList.isEmpty()) binding.tvNoChallenges.visibility = View.VISIBLE else binding.tvNoChallenges.visibility = View.GONE
            }
            Challenge.INACTIVE -> {
                binding.btnInactiveChallenges.isActivated = true
                binding.btnInactiveChallenges.setTextColor(Color.BLACK)

                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
                binding.rvActiveChallenges.visibility = View.INVISIBLE
                binding.rvInactiveChallenges.visibility = View.VISIBLE
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
        AlertDialog.Builder(this).setMessage("Do you want to activate this challenge?")
            .setPositiveButton("Yes")
            { _,_ ->
                addChallenges(position)
            }.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }.show()

    }

    private fun addChallenges(position: Int){
        val inActiveChallenge =  inactiveChallengeList[position]
        showSelectedTab(Challenge.ACTIVE)

        for(i in activeChallengesListForCompare){
            Log.d("Main", "addChallenges: "+i.challengeId)
            if(i.challengeId.trim() == inActiveChallenge.challengeId.trim()){
                Toast.makeText(this,"Challenge already exist!!",Toast.LENGTH_LONG).show()
                return
            }
        }

        val activeChallenges =
            ActiveChallenges(
                inActiveChallenge.challengeId ,
                inActiveChallenge.challengeLogo ,
                inActiveChallenge.challengeTitle ,
                inActiveChallenge.challengeDescription ,
                0)

        FirebaseDatabase.getInstance()
            .getReference("${Utils.User}/${FirebaseAuth.getInstance().uid!!}")
            .child(Utils.ActiveChallenges)
            .push()
            .setValue(activeChallenges)

        loadActiveChallenges()
    }
}


enum class Challenge { ACTIVE, INACTIVE }