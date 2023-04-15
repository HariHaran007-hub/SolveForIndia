package com.zero.golgol.fragment

import android.Manifest
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.zero.golgol.activity.ChallengeActivity
import com.zero.golgol.activity.LeaderboardActivity
import com.zero.golgol.activity.RewardActivity
import com.zero.golgol.databinding.FragmentHomeBinding
import com.zero.golgol.model.Session
import com.zero.golgol.utils.Utils

class HomeFragment : BaseFragment() {
    
    companion object {
        const val  PERMISSION_LOCATION_REQUEST_CODE = 1
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkLocationServices()
        binding.progressBar.progress = 100
        upComingSession()
        clickListeners()
    }

    private fun checkLocationServices(){
        val mLocationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            checkMyPermission()
        }else{
            locationEnableOption()
        }
    }

    private fun clickListeners() {
        binding.ivChallenges.setOnClickListener {
            startActivity(Intent(requireActivity(), ChallengeActivity::class.java))
        }

        binding.ivLeaderboard.setOnClickListener {
            startActivity(Intent(requireActivity(), LeaderboardActivity::class.java))
        }

        binding.ivRewards.setOnClickListener {
            startActivity(Intent(requireActivity(), RewardActivity::class.java))
        }
    }

    private fun checkMyPermission(){
        Dexter.withContext(requireContext())
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

    private fun getLocation(){

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
           checkMyPermission()
            return
        }
        //TODO: Locaval yet to be addws
        val locaVal = FusedLocationProviderClient.KEY_MOCK_LOCATION
        fusedLocationProviderClient.lastLocation.addOnSuccessListener {
            if(it != null){
                val geoCoder = Geocoder(requireContext())
                val currentLocation = geoCoder.getFromLocation(it.latitude,it.longitude,1)
                binding.tvLocation.text = currentLocation.first().locality +", " + currentLocation.first().adminArea
            }else{
                //if there is no internet connection
                binding.tvLocation.text = "No internet connection"
            }

        }
    }

    private fun locationEnableOption(){
        AlertDialog.Builder(context)
            .setMessage("Please enable GPS to proceed")
            .setCancelable(false)
            .setPositiveButton("Open location")
            {
                _,_->
                checkMyPermission()

                requireContext().startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                getLocation()
            }.setNegativeButton("Cancel"){
                dialog,_->
                locationEnableOption()
                dialog.dismiss()
            }.show()
    }

    private fun showRationalDialogForPermissions(){
        AlertDialog.Builder(requireContext()).setMessage("Please enable the required permissions")
            .setPositiveButton("GO TO SETTINGS")
            { _,_ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val  uri = Uri.fromParts("package",requireActivity().packageName,null)
                    intent.data = uri
                    startActivity(intent)
                }catch (e: ActivityNotFoundException){
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun upComingSession(){
        val sessionList = mutableListOf<Session>()
        val databaseReference = FirebaseDatabase.getInstance().reference

        databaseReference.child(Utils.User).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot .exists()){
                    for(c in snapshot.children){
                        sessionList.add(c.getValue(Session::class.java)!!)
                    }
                    update(sessionList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                //
            }
        })
    }

    private fun update(sessionList : MutableList<Session>){
        Log.d("Main", "upComingSession: "+sessionList.size)
    }

    override fun onResume() {
        super.onResume()
        getLocation()
    }


}