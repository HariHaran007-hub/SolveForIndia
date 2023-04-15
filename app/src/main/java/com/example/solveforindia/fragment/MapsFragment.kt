package com.zero.golgol.fragment

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.FirebaseDatabase
import com.zero.golgol.R
import com.zero.golgol.activity.CreateSession1Activity
import com.zero.golgol.activity.CreateSession2Activity
import com.zero.golgol.activity.CreateSession3Activity
import com.zero.golgol.model.SessionAvenue

class MapsFragment : Fragment() {

    private val callback = OnMapReadyCallback { googleMap ->

        FirebaseDatabase.getInstance().getReference("locations").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    for (c in snapshot.children) {
                        val sessionAvenue = c.getValue(SessionAvenue::class.java)
                        googleMap.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    sessionAvenue!!.lat!!,
                                    sessionAvenue.long!!
                                )
                            ).title(sessionAvenue.placeName)
                        )

                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom( LatLng(
                            sessionAvenue!!.lat!!,
                            sessionAvenue.long!!
                        ),8.2f),300,null)
//                        googleMap.moveCamera(
//                            CameraUpdateFactory.newLatLng(
//                                LatLng(
//                                    sessionAvenue!!.lat!!,
//                                    sessionAvenue.long!!
//                                )
//                            )
//                        )
                    }
                }
            }

        googleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(p0: Marker): Boolean {
                val mar = p0.title
                Log.d("Main", "onMarkerClick: $mar")
                promptUserForLocation(p0.title!!, p0.position.latitude, p0.position.longitude)

                return false
            }

        })
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        Log.d(
            "Main",
            "onViewCreated: " + requireArguments().get(CreateSession1Activity.SESSION_NAME)
        )
    }

    private fun promptUserForLocation(placeName: String, lat: Double, long: Double) {
        AlertDialog.Builder(requireContext()).setMessage("Confirm the location $placeName")
            .setPositiveButton("Ok")
            { _, _ ->
                try {

                    val intent = Intent(requireContext(), CreateSession3Activity::class.java)
                    intent.putExtra(CreateSession2Activity.LAT, lat)
                    intent.putExtra(CreateSession2Activity.LONG, long)
                    intent.putExtra(CreateSession2Activity.PLACE_NAME, placeName)
                    intent.putExtra(
                        CreateSession1Activity.SESSION_NAME,
                        requireArguments().getString(CreateSession1Activity.SESSION_NAME)
                    )
                    intent.putExtra(
                        CreateSession1Activity.SESSION_IMAGE,
                        requireArguments().getInt(CreateSession1Activity.SESSION_IMAGE)
                    )
                    startActivity(intent)
                    requireActivity().finish()
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel")
            { dialog, _ ->
                dialog.dismiss()
            }.show()
    }
}