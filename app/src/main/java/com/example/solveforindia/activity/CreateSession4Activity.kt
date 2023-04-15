package com.zero.golgol.activity

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import com.zero.golgol.databinding.ActivityCreateSession4Binding
import com.zero.golgol.fragment.SchedulesFragment
import com.zero.golgol.model.Session
import com.zero.golgol.model.SessionAvenue
import com.zero.golgol.utils.Utils

class CreateSession4Activity : BaseActivity() {

    private lateinit var binding: ActivityCreateSession4Binding
    private lateinit var session : Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateSession4Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        clickListeners()
    }

    private fun clickListeners() {
        binding.btnContinue.setOnClickListener {
            val intent = Intent(this, SessionDetailsActivity::class.java)
            updateDetailsToDataBase()
            val gson = Gson()
            val sessionStringObject = gson.toJson(session)
            intent.putExtra(SchedulesFragment.SESSION_OBJECT,sessionStringObject)
            startActivity(intent)
            finish()
        }

        binding.ivBack.setOnClickListener { finish() }
    }

    private fun updateDetailsToDataBase() {
        val sessionName = intent.getStringExtra(CreateSession1Activity.SESSION_NAME)
        val sessionImage = intent.getIntExtra(CreateSession1Activity.SESSION_IMAGE,0)
        val sessionTime = intent.getStringExtra(CreateSession3Activity.SESSION_TIME)
        val sessionDate = intent.getStringExtra(CreateSession3Activity.SESSION_DATE)

        val sessionPlaceName = intent.getStringExtra(CreateSession2Activity.PLACE_NAME)
        val lat = intent.getDoubleExtra(CreateSession2Activity.LAT,0.0)
        val long = intent.getDoubleExtra(CreateSession2Activity.LONG,0.0)

        val sessionAvenue = SessionAvenue(lat,long,sessionPlaceName!!)

        val playersMap = HashMap<String,String>()//Extract value as string

        playersMap[FirebaseDatabase.getInstance().getReference(Utils.Sessions).push().key.toString()] =
            FirebaseAuth.getInstance().currentUser!!.uid
        val sessionDatabaseReference = FirebaseDatabase.getInstance().getReference(Utils.Sessions)
        val sessionIdentity = sessionDatabaseReference.push().key


        session = Session(sessionImage,sessionAvenue,sessionName!!,sessionDate.toString(),
            sessionTime!!,
            isEquipmentsAvailable = true,
            isSafe = true,
            isPlaymatesAvailable = true,
            players = playersMap,
            createdBy = FirebaseAuth.getInstance().currentUser!!.uid,
            sessionDate = sessionDate!!,
            maxUsers = 4,
            sessionId = sessionIdentity!!
        )

        sessionDatabaseReference.child(sessionIdentity)
            .setValue(session)


//        sessionDatabaseReference.setValue(session)
    }

}