package com.example.zuhauselernen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zuhauselernen.SharedViewModel
import com.example.zuhauselernen.data.local.UserProfile
import com.example.zuhauselernen.data.local.adapter.InvoiceAdapter
import com.example.zuhauselernen.data.local.model.Invoice
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentInvoiceBinding

class InvoiceFragment : Fragment() {
    private lateinit var invoiceAdapter: InvoiceAdapter
    private lateinit var binding:FragmentInvoiceBinding
    private lateinit var userProfile: UserProfile
    private val viewModel: SharedViewModel by activityViewModels()
    private  var userEmailUserInvoices=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userEmailUserInvoices = it.getString("userEmailUserInvoices")!!
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_invoice, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userProfile = UserProfile(requireContext().applicationContext)
        val currentUser=userProfile.getUserProfileByEmail(userEmailUserInvoices)
        invoiceAdapter = InvoiceAdapter(emptyList(), requireContext(),userProfile,currentUser!!)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_Invoice)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = invoiceAdapter
        }
        val userInvoiceData = loadInvoiceData()
        invoiceAdapter.invoices = userInvoiceData
        invoiceAdapter.notifyDataSetChanged()
        binding.btnCancel.setOnClickListener {
            findNavController().navigate(InvoiceFragmentDirections
                .actionInvoiceFragmentToUserSubjectsFragment(
                    userEmailUserSubjects = userEmailUserInvoices))
        }
    }
    /**
     Die Methode gibt zurück eine Liste von Rechnungen, die zum aktuellen Nutzer gehören.
     */
    private fun loadInvoiceData(): List<Invoice> {
        return userProfile.getUserInvoices(userEmailUserInvoices)
    }
}


