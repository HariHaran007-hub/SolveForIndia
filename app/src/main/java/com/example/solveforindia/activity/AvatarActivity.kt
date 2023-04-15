package com.zero.golgol.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.zero.golgol.R
import com.zero.golgol.adapter.AvatarAdapter
import com.zero.golgol.databinding.ActivityAvatarBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.ActiveChallenges
import com.zero.golgol.model.UserDetails
import com.zero.golgol.utils.Utils

class AvatarActivity : BaseActivity(), ClickListener {

    private lateinit var binding: ActivityAvatarBinding
    private lateinit var adapter: AvatarAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAvatarBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        showAvatars()
    }

    private fun showAvatars() {
        adapter = AvatarAdapter(this, getAvatars(), this)

        binding.rvAvatars.setHasFixedSize(true)
        binding.rvAvatars.layoutManager =  StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        binding.rvAvatars.adapter = adapter
    }

    private fun getAvatars(): MutableList<Int> {
        val avatarList = mutableListOf<Int>()

        avatarList.add(R.drawable.img_avatar_1)
        avatarList.add(R.drawable.img_avatar_2)
        avatarList.add(R.drawable.img_avatar_3)
        avatarList.add(R.drawable.img_avatar_4)
        avatarList.add(R.drawable.img_avatar_5)
        avatarList.add(R.drawable.img_avatar_6)
        avatarList.add(R.drawable.img_avatar_7)
        avatarList.add(R.drawable.img_avatar_8)
        avatarList.add(R.drawable.img_avatar_9)
        avatarList.add(R.drawable.img_avatar_10)
        avatarList.add(R.drawable.img_avatar_11)
        avatarList.add(R.drawable.img_avatar_12)

        return avatarList
    }

    override fun buttonClick() {
        //
    }

    override fun buttonClick(position: Int, type: String) {
    //
    }

    override fun buttonClick(position: Int) {
        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            binding.lnrContents.visibility = View.GONE
            binding.tvProfileCompleted.visibility = View.VISIBLE
            Handler().postDelayed({

            }, 2000)

        }, 500)
        saveUserDetails(position)
    }


    private fun saveUserDetails(avatarImagePosition: Int) {
        val userName = intent.getStringExtra(CreateProfileActivity.USER_NAME)
        val userGender = intent.getStringExtra(CreateProfileActivity.USER_GENDER)
        val userAge = intent.getStringExtra(CreateProfileActivity.USER_AGE)
        val userIdCardNumber = intent.getStringExtra(CreateProfileActivity.USER_ID_CARD_NUMBER)
        val userPhoneNumber = intent.getStringExtra(CreateProfileActivity.PHONE_NUMBER)
        val loginType  = intent.getStringExtra("login_type")
        val avatar = getAvatars()[avatarImagePosition]
        val reference = FirebaseDatabase.getInstance().getReference(Utils.User)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val map = HashMap<String,ActiveChallenges>()
        val golPalsMap = HashMap<String, String>()

        val user = UserDetails(userId,userName!!,userAge!!,userGender!!,userIdCardNumber!!,userPhoneNumber!!,loginType!!,avatar,0
            ,map,0,golPalsMap)
        Log.d("Avatar", "saveUserDetails: $user")

        reference.child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(user).addOnCompleteListener {
            if(it.isSuccessful){
                Toast.makeText(this,"Account saved successfully",Toast.LENGTH_LONG).show()
                val intent  =Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}