package com.zero.golgol.activity

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.core.content.res.ResourcesCompat
import com.google.firebase.auth.FirebaseAuth
import com.zero.golgol.R
import com.zero.golgol.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val colorFrom = ResourcesCompat.getColor(resources, R.color.orange, null)
        val colorTo = ResourcesCompat.getColor(resources, R.color.white, null)
        val colorAnimation = ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, colorTo)
        colorAnimation.duration = 2000 // milliseconds

        colorAnimation.addUpdateListener { animator -> binding.rlRoot.setBackgroundColor(animator.animatedValue as Int) }
        colorAnimation.addUpdateListener { animator -> binding.rlRoot.setBackgroundColor(animator.animatedValue as Int) }

        Handler().postDelayed({
            colorAnimation.start()
        }, 500)

        Handler().postDelayed({
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            binding.ivLogoOrange.elevation = 5F
            binding.tvWelcome.visibility = View.VISIBLE

            Handler().postDelayed({
                if(FirebaseAuth.getInstance().currentUser != null){

                    fetchUserDetails(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    user.userId = FirebaseAuth.getInstance().currentUser!!.uid
//            FirebaseDatabase.getInstance().getReference("users").child(FirebaseAuth.getInstance().currentUser!!.uid)
//                .get().addOnSuccessListener {snapshot->
//                    if(snapshot.exists() ){
//                        startActivity(Intent(this,HomeActivity::class.java))
//                        finish()
//                    }else{
//                        startActivity(Intent(this,GoogleSignInUserDetails::class.java))
//                        finish()
//                    }
//                }

                    startActivity(Intent(this,HomeActivity::class.java))
                    finish()
                }else{
                    startActivity(Intent(this, WelcomeActivity::class.java))
                    finish()
                }
            }, 500)

        }, 1800)
    }
}