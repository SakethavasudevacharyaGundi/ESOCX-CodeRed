package com.saketh.legalaid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.saketh.legalaid.R
import com.saketh.legalaid.model.LawyerProfile

class LawyerProfileAdapter(
    private val onContactClick: (LawyerProfile) -> Unit
) : RecyclerView.Adapter<LawyerProfileAdapter.LawyerProfileViewHolder>() {

    private var lawyers: List<LawyerProfile> = emptyList()

    fun updateLawyers(newLawyers: List<LawyerProfile>) {
        lawyers = newLawyers
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LawyerProfileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lawyer_profile, parent, false)
        return LawyerProfileViewHolder(view)
    }

    override fun onBindViewHolder(holder: LawyerProfileViewHolder, position: Int) {
        holder.bind(lawyers[position])
    }

    override fun getItemCount() = lawyers.size

    inner class LawyerProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.lawyerName)
        private val specialization: TextView = itemView.findViewById(R.id.lawyerSpecialization)
        private val experience: TextView = itemView.findViewById(R.id.experience)
        private val successRate: TextView = itemView.findViewById(R.id.successRate)
        private val rating: TextView = itemView.findViewById(R.id.rating)
        private val contactButton: MaterialButton = itemView.findViewById(R.id.contactButton)

        fun bind(lawyer: LawyerProfile) {
            name.text = lawyer.name
            specialization.text = lawyer.specialization
            experience.text = lawyer.experience
            successRate.text = lawyer.successRate
            rating.text = lawyer.rating
            contactButton.setOnClickListener { onContactClick(lawyer) }
        }
    }
} 