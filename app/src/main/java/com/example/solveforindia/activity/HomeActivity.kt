package com.zero.golgol.activity

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.zero.golgol.R
import com.zero.golgol.databinding.ActivityHomeBinding
import com.zero.golgol.fragment.ExploreFragment
import com.zero.golgol.fragment.HomeFragment
import com.zero.golgol.fragment.ProfileFragment
import com.zero.golgol.fragment.SchedulesFragment

//TODO: Internet popup yet to be added
class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val fragmentManager = supportFragmentManager
    private var fragmentContainer = 0

    private val homeFragment = HomeFragment()
    private var activeFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        checkMyPermission();
        fragmentContainer = R.id.flFragmentContainer
        addFragment(homeFragment, false)
        clickListeners()
    }

    private fun checkMyPermission(){
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object  : PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Log.d("TAG", "onPermissionGranted: " +p0.toString())
                    Toast.makeText(applicationContext,"Permission granted",Toast.LENGTH_LONG).show()
                }
                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    showRationalDialogForPermissions()
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {
                   showRationalDialogForPermissions()
                }

            }).check()
    }


    private fun showRationalDialogForPermissions(){
        //TODO: // Api integration yet to deployed
        AlertDialog.Builder(this).setMessage("Please enable the required permissions")
            .setPositiveButton("GO TO SETTINGS")
            { _,_ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val  uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel")
            {
                    dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun clickListeners() {
        binding.ivHome.setOnClickListener {
            if (homeFragment.tag != null && !binding.ivHome.isActivated)
                onBackPressed()
            else
                replaceFragment(homeFragment, false)
        }

        binding.ivExplore.setOnClickListener {
            val addToBackStack: Boolean = activeFragment == homeFragment
            replaceFragment(ExploreFragment(), addToBackStack)
        }

        binding.ivSchedules.setOnClickListener {
            val addToBackStack: Boolean = activeFragment == homeFragment
            replaceFragment(SchedulesFragment(), addToBackStack)
        }

        binding.ivProfile.setOnClickListener {
            val addToBackStack: Boolean = activeFragment == homeFragment
            replaceFragment(ProfileFragment(), addToBackStack)
        }

    }

    fun addFragment(fragment: Fragment, addToBackStack: Boolean) { //Add fragments
        val tag = fragment.javaClass.name
        if (activeFragment?.javaClass?.name != tag) {
            val transaction = fragmentManager.beginTransaction()
            if (addToBackStack) transaction.addToBackStack(tag)

            transaction.replace(fragmentContainer, fragment, tag).commit()
            fragmentManager.executePendingTransactions()
            activeFragment = fragment
        }
        updateNavBar(tag)
    }

    fun replaceFragment(fragment: Fragment, addToBackStack: Boolean) { //Replace fragments
        val tag = fragment.javaClass.name
        val fragmentPopped = fragmentManager.popBackStackImmediate(tag, 0)
        if (activeFragment?.javaClass?.name != tag && !fragmentPopped) {
            val transaction = fragmentManager.beginTransaction()
            if (addToBackStack) transaction.addToBackStack(tag)
            transaction.replace(fragmentContainer, fragment, tag).commit()
            fragmentManager.executePendingTransactions()
            activeFragment = fragment
        }
        updateNavBar(tag)
    }

    private fun updateNavBar(tag: String) { //Show the active fragment in the navbar by highlighting the selected fragment
        setAllInactive()

        when (tag) {
            homeFragment.javaClass.name -> {
                binding.ivTabActive1.visibility = View.VISIBLE
                binding.ivHome.isActivated = true
            }
            ExploreFragment().javaClass.name -> {
                binding.ivTabActive2.visibility = View.VISIBLE
                binding.ivExplore.isActivated = true
            }
            SchedulesFragment().javaClass.name -> {
                binding.ivTabActive3.visibility = View.VISIBLE
                binding.ivSchedules.isActivated = true
            }
            ProfileFragment().javaClass.name -> {
                binding.ivTabActive4.visibility = View.VISIBLE
                binding.ivProfile.isActivated = true
            }
        }
    }

    private fun setAllInactive() {
        binding.ivTabActive1.visibility = View.INVISIBLE
        binding.ivTabActive2.visibility = View.INVISIBLE
        binding.ivTabActive3.visibility = View.INVISIBLE
        binding.ivTabActive4.visibility = View.INVISIBLE

        binding.ivHome.isActivated = false
        binding.ivExplore.isActivated = false
        binding.ivSchedules.isActivated = false
        binding.ivProfile.isActivated = false
    }

    override fun onBackPressed() {
        super.onBackPressed()
        activeFragment = fragmentManager.findFragmentById(fragmentContainer)
//        showOrHideNavBar()

        if (activeFragment is HomeFragment || activeFragment is ExploreFragment
            || activeFragment is SchedulesFragment || activeFragment is ProfileFragment) { //Show active tab in navbar
            updateNavBar(activeFragment?.javaClass?.name.toString())
        }
    }
}