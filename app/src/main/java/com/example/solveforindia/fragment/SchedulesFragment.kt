package com.zero.golgol.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.google.gson.Gson
import com.zero.golgol.activity.CreateSession1Activity
import com.zero.golgol.activity.SessionDetailsActivity
import com.zero.golgol.adapter.SessionAdapter
import com.zero.golgol.databinding.FragmentSchedulesBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Session
import com.zero.golgol.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class SchedulesFragment : BaseFragment(), ClickListener {

    companion object {
        const val SESSION_OBJECT = "session_object"
    }

    private var _binding: FragmentSchedulesBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendar: Calendar
    private lateinit var adapter: SessionAdapter
    private lateinit var sessionList: MutableList<Session>

    private lateinit var databaseReference : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        calendar = Calendar.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference(Utils.Sessions)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSchedulesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        loadSessionDetails()
        clickListeners()
    }

    private fun clickListeners() {
        binding.ivAddSession.setOnClickListener { startActivity(Intent(requireActivity(), CreateSession1Activity::class.java)) }
    }

    private fun loadSessionDetails() {
        binding.layoutProgress.progressBarHolder.visibility = View.VISIBLE

        databaseReference.get().addOnSuccessListener { snapshot->
            if(snapshot.exists()) {
                sessionList.clear()
                for (c in snapshot.children){
                    val session = c.getValue(Session::class.java)

                    val sessionDateString = session?.sessionDate
                    val sessionTimeString = (session?.sessionTime)!!.split("-")[1].trim()

                    val dateFormat = "EEE, MMM dd, yyyy"
                    val timeFormat = "hh:mm a"

                    val dateSdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                    val timeSdf = SimpleDateFormat(timeFormat, Locale.getDefault())

                    val sessionDate = dateSdf.parse(sessionDateString!!)
                    val currentDate = dateSdf.format(calendar.time)
                    val strCurrentTime = timeSdf.format(calendar.time)
                    val currentTime = timeSdf.parse(strCurrentTime)?.time

                    if (calendar.time.time < sessionDate?.time!!){
                        sessionList.add(session)
                    }
                    else if (sessionDateString == currentDate.toString() && currentTime!! < getSessionTime(sessionTimeString)!!){
                        sessionList.add(session)
                    }
                    else {
                        removeSession(session.sessionId)
                    }
                }
                adapter.updateItemList(sessionList)
                binding.layoutProgress.progressBarHolder.visibility = View.INVISIBLE
            } else{
                binding.layoutProgress.progressBarHolder.visibility = View.INVISIBLE
            }

        }

//        databaseReference.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(snapshot.exists()) {
//                    sessionList.clear()
//                    for (c in snapshot.children){
//                        val session = c.getValue(Session::class.java)
//
//                        val sessionDateString = session?.sessionDate
//                        val sessionTimeString = (session?.sessionTime)!!.split("-")[1].trim()
//
//                        val dateFormat = "EEE, MMM dd, yyyy"
//                        val timeFormat = "hh:mm a"
//
//                        val dateSdf = SimpleDateFormat(dateFormat, Locale.getDefault())
//                        val timeSdf = SimpleDateFormat(timeFormat, Locale.getDefault())
//
//                        val sessionDate = dateSdf.parse(sessionDateString!!)
//                        val currentDate = dateSdf.format(calendar.time)
//                        val strCurrentTime = timeSdf.format(calendar.time)
//                        val currentTime = timeSdf.parse(strCurrentTime)?.time
//
//                        if (calendar.time.time < sessionDate?.time!!){
//                            sessionList.add(session)
//                        }
//                        else if (sessionDateString == currentDate.toString() && currentTime!! < getSessionTime(sessionTimeString)!!){
//                            sessionList.add(session)
//                        }
//                        else {
//                            removeSession(session.sessionId)
//                        }
//                    }
//                    adapter.updateItemList(sessionList)
//                    binding.layoutProgress.progressBarHolder.visibility = View.INVISIBLE
//                } else{
//                    binding.layoutProgress.progressBarHolder.visibility = View.INVISIBLE
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//        })

    }

    private fun setUpRecyclerView(){
        sessionList = mutableListOf()
        adapter = SessionAdapter(requireContext(), sessionList, this)
        binding.rvSessions.setHasFixedSize(true)
        binding.rvSessions.layoutManager =  LinearLayoutManager(requireContext())
        binding.rvSessions.adapter = adapter
    }

    override fun buttonClick() {
        //
    }

    override fun buttonClick(position: Int, type: String) {
        if (type == "Session Details"){
            val intent = Intent(requireActivity(),SessionDetailsActivity::class.java)
            val gson = Gson()
            val session = gson.toJson(sessionList[position])
            Log.d("Main", "buttonClick: $session")
            intent.putExtra(SESSION_OBJECT,session.toString())
            startActivity(intent)
        }
    }

    override fun buttonClick(position: Int) {
        Log.d("Main", "buttonClick: ${sessionList[position].maxUsers}")
    }

    private fun removeSession(sessionId: String) {
        databaseReference.child(sessionId).removeValue()
    }

    private fun getSessionTime(sessionTimeString : String?) : Long? {
        val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
        try {
           return sdf.parse(sessionTimeString!!)?.time
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}