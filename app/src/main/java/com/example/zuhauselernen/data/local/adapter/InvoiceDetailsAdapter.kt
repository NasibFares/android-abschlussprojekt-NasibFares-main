package com.example.zuhauselernen.data.local.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.model.Subject
import com.examples.zuhauselernen.databinding.InvoiceListDetailsBinding

class InvoiceDetailsAdapter(
    var subjects: List<Subject>,
    private val email: String,
    private val invoiceNumber: Int,
    private val context: Context,
    private val userProfile: UserProfile
) :
    RecyclerView.Adapter<InvoiceDetailsAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val binding: InvoiceListDetailsBinding) :
        RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = InvoiceListDetailsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {


        val subject = subjects[position]
        holder.binding.tvSubjectName.text=context.getString(subject.subjectName.toInt())
        holder.binding.ivSubscriptionSubjectImage.setImageResource(subject.subjectImage)
        if(subject.isMonthlySubscribed) {
            holder.binding.tvSubscriptionType.text="Monatlich"
        }else if(subject.isYearlySubscribed){
            holder.binding.tvSubscriptionType.text="Jährlich"
        }else{
            holder.binding.tvSubscriptionType.text="Kostenlos"
        }
        holder.binding.tvSubscriptionPrice.text=subject.subscriptionPrice.toString()
    }

    override fun getItemCount(): Int {
        return subjects.size
    }


}