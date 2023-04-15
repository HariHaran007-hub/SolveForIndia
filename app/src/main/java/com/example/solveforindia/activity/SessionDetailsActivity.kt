package com.zero.golgol.activity

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.zero.golgol.databinding.ActivitySessionDetailsBinding
import com.zero.golgol.fragment.SchedulesFragment
import com.zero.golgol.model.Session
import com.zero.golgol.utils.Utils
import java.math.RoundingMode


class SessionDetailsActivity : BaseActivity() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var binding: ActivitySessionDetailsBinding

    private lateinit var sessionUserList: MutableList<String>
    private lateinit var golPalsList : MutableList<String>

    private lateinit var session: Session


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySessionDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        setContentView(view)
        checkLocationServices()

        init()
        reloadDataBase()
        updateUi()
        clickListeners()
    }

    private fun init() {
        sessionUserList = mutableListOf()
        val gson = Gson()
        val sessionText = intent.getStringExtra(SchedulesFragment.SESSION_OBJECT)
        session = gson.fromJson(sessionText, Session::class.java)
    }

    private fun clickListeners() {
        binding.ivBack.setOnClickListener { finish() }

        binding.lnrShowChat.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.root, AutoTransition())
            if (binding.rlChat.visibility == View.VISIBLE) {
                binding.rlChat.visibility = View.GONE
            } else {
                binding.rlChat.visibility = View.VISIBLE
                binding.scrollView.post { binding.scrollView.fullScroll(View.FOCUS_DOWN) }
            }
        }

        if(sessionUserList.contains(FirebaseAuth.getInstance().currentUser!!.uid)){
            binding.btnContinue.visibility  = View.GONE
            binding.tvAlreadyExist.visibility = View.VISIBLE
        }

        binding.btnContinue.setOnClickListener {
           promptUser()
        }

        binding.mapNavigiate.setOnClickListener {
            openGoogleMapApplication()
        }
        //
    }

    private fun executeProcess(){
        if (sessionUserList.contains(FirebaseAuth.getInstance().currentUser!!.uid)) {
            //nothing happen since user already exist
//                Toast.makeText(
//                    applicationContext,
//                    "User already exist in session",
//                    Toast.LENGTH_LONG
//                ).show()
            Log.d("Main", "clickListeners: already exist")
        } else {
            binding.tvAlreadyExist.visibility = View.VISIBLE
            binding.btnContinue.visibility  = View.GONE
            updateDatabase()
        }
    }


    private fun promptUser() {
        AlertDialog.Builder(this).setMessage("Confirm to enroll for this session")
            .setPositiveButton("Ok")
            { _, _ ->
                executeProcess()

            }.setNegativeButton("Cancel")
            { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun openGoogleMapApplication() {
        val geoUri =
            "http://maps.google.com/maps?q=loc:" + session.sessionAvenue!!.lat + "," + session.sessionAvenue!!.long + " (" + session.sessionAvenue!!.placeName.toString() + ")"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(geoUri))

        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }

    private fun updateDatabase() {
        val databaseSessionReference = FirebaseDatabase.getInstance()
            .getReference("${Utils.Sessions}/${session.sessionId.toString()}")

        when {
            session.maxUsers!! >= sessionUserList.size -> {
                val map = HashMap<String, Any>()
                map[databaseSessionReference.push().key.toString()] = FirebaseAuth.getInstance().currentUser!!.uid
                databaseSessionReference.child("players").push().setValue(FirebaseAuth.getInstance().currentUser!!.uid)
                updateGolPal()


                reloadDataBase()
            }
            sessionUserList.contains(FirebaseAuth.getInstance().currentUser!!.uid) -> {
                Toast.makeText(
                    applicationContext,
                    "You have already enrolled for this session",
                    Toast.LENGTH_LONG
                ).show()
//                binding.btnContinue.visibility  = View.GONE
            }
            else -> {
                Toast.makeText(applicationContext, "Oops!! session is full", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun reloadDataBase() {
        val databaseReference = FirebaseDatabase.getInstance()
            .getReference("${Utils.Sessions}/${session.sessionId}/players")
        databaseReference.get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                sessionUserList.clear()
                val value: HashMap<String, String> = snapshot.value as HashMap<String, String>
                for (c in value.values) {
                    sessionUserList.add(c)
                }
            }
        }
    }

    private fun updateUi() {
        for (c in session.players!!.values) {
            sessionUserList.add(c)
        }

        binding.tvTitle.text = session.sessionName

        binding.tvDate.text = session.sessionDate
        binding.tvTime.text = session.sessionTime
        Log.d("Main", "updateUi: "+ session.sessionAvenue!!.placeName)
        binding.tvAvenue.text = session.sessionAvenue!!.placeName.toString()
        if (sessionUserList.contains(user.userId)) {
            Glide.with(applicationContext)
                .asDrawable()
                .load(user.avatar.toInt())
                .into(binding.ivMyImage)
        }

        //avatar to be added
        var i = 0
        for (sessionUser in sessionUserList) {
            if(sessionUser != FirebaseAuth.getInstance().currentUser!!.uid){
                i++
                if(i == 1 ){
                    binding.ivUserImage1.visibility = View.VISIBLE
                    FirebaseDatabase.getInstance().getReference("${Utils.User}/${sessionUser}/avatar").get()
                        .addOnSuccessListener {
                            val avatar = it.value as Long
                            Glide.with(this)
                                .asDrawable()
                                .load(avatar.toInt())
                                .into(binding.ivUserImage1)
                        }
                }
                if(i == 2){
                    binding.ivUserImage2.visibility = View.VISIBLE
                    FirebaseDatabase.getInstance().getReference("${Utils.User}/${sessionUser}/avatar").get()
                        .addOnSuccessListener {
                            val avatar = it.value as Long
                            Glide.with(this)
                                .asDrawable()
                                .load(avatar.toInt())
                                .into(binding.ivUserImage2)
                        }
                }

                if(i == 3){

                    binding.ivUserImage3.visibility = View.VISIBLE

                    FirebaseDatabase.getInstance().getReference("${Utils.User}/${sessionUser}/avatar").get()
                        .addOnSuccessListener {
                            val avatar = it.value as Long
                            Glide.with(this)
                                .asDrawable()
                                .load(avatar.toInt())
                                .into(binding.ivUserImage3)
                        }
                }

                if(i == 4){
                    binding.ivUserImage4.visibility = View.VISIBLE

                    FirebaseDatabase.getInstance().getReference("${Utils.User}/${sessionUser}/avatar").get()
                        .addOnSuccessListener {
                            val avatar = it.value as Long
                            Glide.with(this)
                                .asDrawable()
                                .load(avatar.toInt())
                                .into(binding.ivUserImage4)
                        }
                }

               if(i > 4){
                    binding.tvMoreCount.visibility = View.VISIBLE
                    binding.tvMoreCount.text = (sessionUserList.size - 4).toString()
                }
            }
        }
    }

    private fun checkLocationServices(){
        val mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            checkMyPermission()
        }else{
            locationEnableOption()
        }
    }
    private fun checkMyPermission(){
        Dexter.withContext(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object  : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    getLocation()
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
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel")
            {
                    dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun getLocation(){

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkMyPermission()
            return
        }
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if(it != null){
                session.sessionAvenue!!.lat?.let { it1 ->
                    session.sessionAvenue!!.long?.let { it2 ->
                        val loc1 = Location("")
                        loc1.latitude = it.latitude
                        loc1.longitude = it.longitude
                        val loc2 = Location("")
                        loc2.latitude = it1
                        loc2.longitude = it2
                        val distanceInMeters = loc1.distanceTo(loc2)

                        val time = (distanceInMeters/1000)/5
                        Log.d("Main", "getLocation: ${time * 60}")

                        val rounded = (distanceInMeters/1000).toBigDecimal().setScale(1, RoundingMode.UP).toDouble()
                        binding.tvDistance.text = rounded.toString() + "km"
                        binding.tvDistanceInTime.text = ((time * 60).toInt()).toString() + " minutes in walk"
                    }
                }
            }else{
                //TODO: If there is no internet connection
            }
        }
    }

    private fun locationEnableOption(){
        AlertDialog.Builder(this)
            .setMessage("GPS Not enabled")
            .setPositiveButton("Open location")
            {
                    _,_->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }.setNegativeButton("Cancel"){
                    dialog,_->
                locationEnableOption()
                dialog.dismiss()
            }.show()
    }

    private fun updateGolPal(){
        val myUserId = FirebaseAuth.getInstance().currentUser!!.uid
        for(s in sessionUserList){
            if (s != myUserId) {
                FirebaseDatabase.getInstance()
                    .getReference("${Utils.User}/${myUserId}/${Utils.GolPals}")
                    .child(s).setValue(s)
            }
        }

        for (s in sessionUserList){
            if (s != FirebaseAuth.getInstance().currentUser!!.uid) {
                FirebaseDatabase.getInstance()
                    .getReference("${Utils.User}/${s}/${Utils.GolPals}")
                    .child(myUserId).setValue(FirebaseAuth.getInstance().currentUser!!.uid)
            }
        }
    }
}