package com.example.zuhauselernen.data.local.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.model.Invoice
import com.example.zuhauselernen.data.local.model.UserData
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.InvoiceListItemsBinding

class InvoiceAdapter(
    var invoices: List<Invoice>,
    private val context: Context,
    private val userProfile: UserProfile,
    private val currentUser: UserData,
    ) : RecyclerView.Adapter<InvoiceAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: InvoiceListItemsBinding) :

        RecyclerView.ViewHolder(binding.root){
        init {
            binding.innerRecyclerView.layoutManager = LinearLayoutManager(context)

        }
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = InvoiceListItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val invoice = invoices[position]
        holder.binding.innerRecyclerView.visibility=View.GONE
        holder.binding.tvInvoiceDate.text=invoice.invoiceDate.toString()
        holder.binding.tvInvoiceNumber.text=invoice.invoiceNumber.toString()

            holder.binding.tvInvoicePaymentMethod.text =
                context.getString(invoice.payment.paymentName.toInt())
        val roundedInvoiceNumber = String.format("%.2f", invoice.invoiceSubtotal)
        holder.binding.tvInvoiceSubtotal.text=roundedInvoiceNumber
        holder.binding.ivInvoiceLogo.setImageResource(R.drawable.invoice_bill)
        invoices=userProfile.getUserInvoices(currentUser.emailAdress)
        val subjects = userProfile.getInvoiceSubjectsByInvoiceId(currentUser.emailAdress, invoice.invoiceNumber)

        var isExpanded = invoice.isExpanded
        holder.binding.materialCardView.setOnClickListener {
            println("materialCard is being clicked")
            invoice.isExpanded = !isExpanded
            val innerAdapter = InvoiceDetailsAdapter(subjects,currentUser.emailAdress,
                invoice.invoiceNumber,
                context,
                userProfile)
            holder.binding.innerRecyclerView.adapter = innerAdapter
        if (isExpanded) {
            holder.binding.innerRecyclerView.visibility = View.VISIBLE
            println(holder.binding.innerRecyclerView.visibility)
            isExpanded=false
            innerAdapter.notifyItemChanged(position)
        } else {
            holder.binding.innerRecyclerView.visibility = View.GONE
            println(holder.binding.innerRecyclerView.visibility)
            innerAdapter.notifyItemChanged(position)
            isExpanded=true
        }

    }
    }

    override fun getItemCount(): Int {
        return invoices.size
    }


}

