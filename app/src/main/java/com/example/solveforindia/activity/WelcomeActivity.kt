package com.zero.golgol.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.zero.golgol.R
import com.zero.golgol.adapter.SplashAdapter
import com.zero.golgol.databinding.ActivityWelcomeBinding
import com.zero.golgol.listener.ClickListener

class WelcomeActivity : BaseActivity(), ClickListener {

    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initViews()
    }

    private fun initViews() {
        //Setup view page adapter for images
        val pagerAdapter = SplashAdapter(this, this)
        binding.vpSplashImages.adapter = pagerAdapter

        binding.ivPageIndicator1.setOnClickListener { binding.vpSplashImages.currentItem = 0 }
        binding.ivPageIndicator2.setOnClickListener { binding.vpSplashImages.currentItem = 1 }
        binding.ivPageIndicator3.setOnClickListener { binding.vpSplashImages.currentItem = 2 }

        binding.vpSplashImages.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) { //Image change listener for updating layout components
                when (position) {
                    0 -> {
                        binding.ivPageIndicator1.setImageResource(R.drawable.img_page_indicator_active)
                        binding.ivPageIndicator2.setImageResource(R.drawable.img_page_indicator_inactive)
                        binding.ivPageIndicator3.setImageResource(R.drawable.img_page_indicator_inactive)

                        binding.btnCreate.text = "Play"
                    }
                    1 -> {
                        binding.ivPageIndicator2.setImageResource(R.drawable.img_page_indicator_active)
                        binding.ivPageIndicator1.setImageResource(R.drawable.img_page_indicator_inactive)
                        binding.ivPageIndicator3.setImageResource(R.drawable.img_page_indicator_inactive)

                        binding.btnCreate.text = "Create"
                    }
                    2 -> {
                        binding.ivPageIndicator3.setImageResource(R.drawable.img_page_indicator_active)
                        binding.ivPageIndicator2.setImageResource(R.drawable.img_page_indicator_inactive)
                        binding.ivPageIndicator1.setImageResource(R.drawable.img_page_indicator_inactive)

                        binding.btnCreate.text = "Let's Gol!"
                    }
                }
            }
        })

        binding.btnCreate.setOnClickListener { buttonClick() }
    }

    override fun buttonClick() {
        when (binding.vpSplashImages.currentItem) {
            0 -> binding.vpSplashImages.currentItem = 1
            1 -> binding.vpSplashImages.currentItem = 2
            2 -> {
                TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
                binding.vpSplashImages.visibility = View.INVISIBLE
                binding.lnrPageIndicator.visibility = View.INVISIBLE
                binding.btnCreate.visibility = View.INVISIBLE
                binding.tvHeading.visibility = View.VISIBLE

                Handler().postDelayed({
                    startActivity(Intent(this, CreateProfileActivity::class.java))
                    finish()
                }, 1250)
            }
        }
    }

    override fun buttonClick(position: Int, type: String) {
        //
    }

    override fun buttonClick(position: Int) {
        //
    }
}