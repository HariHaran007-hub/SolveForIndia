package com.zero.golgol.activity

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.zero.golgol.controller.User
import com.zero.golgol.model.UserDetails

open class BaseActivity: AppCompatActivity() {

    val user: User = User.getInstance()


    fun fetchUserDetails(userId: String) {
        FirebaseDatabase.getInstance().getReference("users").child(userId)
            .get().addOnSuccessListener {snapshot->
                if(snapshot.exists()){
                    val userDetails = snapshot.getValue(UserDetails::class.java)

                    user.age = userDetails?.userAge
                    user.username = userDetails?.userName
                    user.userId = userDetails?.userId
                    user.avatar = userDetails!!.avatar.toString()
                    user.gender = userDetails.userGender
                    user.idNumber = userDetails.userIdCardNumber
                    user.phoneNumber = userDetails.userPhoneNumber
                    user.gols = userDetails.gols ?: 0
                    user.badges = userDetails.badges ?: 0
                    user.challenges = userDetails.activeChallenges
                    user.golPals = userDetails.golPals

                    Log.d("TAG", "fetchDetails: $user")
                }
            }
    }

}