package com.zero.golgol.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.zero.golgol.R
import com.zero.golgol.databinding.ItemSessionBinding
import com.zero.golgol.listener.ClickListener
import com.zero.golgol.model.Session

class SessionAdapter(private val context: Context, private var sessionList: MutableList<Session>,
                     private val listener: ClickListener): RecyclerView.Adapter<SessionAdapter.ViewHolder>() {

    private var _binding: ItemSessionBinding? = null
    private val binding get() = _binding!!

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _binding = ItemSessionBinding.inflate(LayoutInflater.from(context), parent, false)
        val view = binding.root
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val session = sessionList[position]

        Glide.with(context)
            .asDrawable()
            .load(session.sessionImage)
            .into(binding.ivSession)

        binding.tvAvenue.text = session.sessionAvenue!!.placeName
        binding.tvSessionTitle.text = session.sessionName
        binding.tvAvenueDistance.text = session.sessionDistance
        binding.tvSessionTime.text = session.sessionTime
        binding.ivEquipmentStatus.isEnabled = session.isEquipmentsAvailable!!
        binding.ivSafetyStatus.isEnabled = session.isSafe!!
        binding.ivPlaymatesStatus.isEnabled = session.isPlaymatesAvailable!!

        if (session.createdBy == FirebaseAuth.getInstance().uid!!){
            binding.root.background =
                ResourcesCompat.getDrawable(context.resources, R.drawable.bg_white_rounded_corners_green, null)
        }
        else{
            binding.root.background =
                ResourcesCompat.getDrawable(context.resources, R.drawable.bg_white_rounded_corners_orange, null)
        }

        binding.root.setOnClickListener {
            listener.buttonClick(position, "Session Details")
        }
    }

    override fun getItemCount(): Int {
        return sessionList.size
    }

    fun updateItemList(sessionList: MutableList<Session>){
        this.sessionList = sessionList
        notifyDataSetChanged()
    }

}