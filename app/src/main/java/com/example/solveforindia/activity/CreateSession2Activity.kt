package com.zero.golgol.activity

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.webkit.PermissionRequest
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.zero.golgol.R
import com.zero.golgol.databinding.ActivityCreateSession2Binding
import com.zero.golgol.fragment.MapsFragment

class CreateSession2Activity : BaseActivity() {

    companion object {
        const val LAT = "lat"
        const val LONG = "long"
        const val PLACE_NAME = "place_name"
    }

    private lateinit var binding: ActivityCreateSession2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSession2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        checkMyPermission()

        val mapFragment = MapsFragment()
        val data = Bundle()
        data.putString(CreateSession1Activity.SESSION_NAME,
            intent.getStringExtra(CreateSession1Activity.SESSION_NAME))
        data.putInt(CreateSession1Activity.SESSION_IMAGE,
            intent.getIntExtra(CreateSession1Activity.SESSION_IMAGE,0))
        mapFragment.arguments = data

        supportFragmentManager.beginTransaction().replace(R.id.mapFragmentContainer,mapFragment).commit()

//        Handler().postDelayed({
//           // Log.d("Main", "onCreate: "+sessionDetails.sessionImage)
//            val intent = Intent(this,CreateSession3Activity::class.java)
//            intent.putExtra(CreateSession1Activity.SESSION_NAME
//                ,getIntent().getStringExtra(CreateSession1Activity.SESSION_NAME))
//
//            intent.putExtra(CreateSession1Activity.SESSION_IMAGE
//                ,getIntent().getIntExtra(CreateSession1Activity.SESSION_IMAGE,0))
//           "Dindigul mvm park intent.putExtra(LAT,1.922)\n" +
//                    "            intent.putExtra(LONG,3.222)\n" +
//                    "            intent.putExtra(PLACE_NAME,")
//            startActivity(intent)
//            finish()
//        }, 2000)
        clickListeners()
    }



    private fun clickListeners() {
        binding.ivBack.setOnClickListener { finish() }
    }

    private fun checkMyPermission(){
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object  : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    Toast.makeText(applicationContext,"Permission granted", Toast.LENGTH_LONG).show()
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

        AlertDialog.Builder(this).setMessage("Please enable the required permissions")
            .setPositiveButton("GO TO SETTINGS")
            { _,_ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val  uri = Uri.fromParts("package",packageName,null)
                    intent.data = uri
                    startActivity(intent)
                    finish()
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel")
            {
                    dialog, _ ->
                dialog.dismiss()
            }.show()

    }

}