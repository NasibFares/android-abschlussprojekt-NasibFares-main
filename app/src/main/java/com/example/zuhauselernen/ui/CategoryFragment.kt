package com.example.zuhauselernen.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.zuhauselernen.SharedViewModel
import com.example.zuhauselernen.data.local.adapter.CategoryAdapter
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.FragmentCategoryBinding


class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private val viewModel: SharedViewModel by activityViewModels()
    private var subjectId: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            subjectId = it.getInt("subjectId")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /**
         Die Liste der Kategorie wird wird gefiltert, um die Kategorie ,die zur bestimmten Subject
         anzeigen zu lassen.
         */
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            val filteredList = categories.filter { it.categorySubject == subjectId }
            binding.rvCategory.adapter = CategoryAdapter(requireContext(), filteredList)


        }

    }
}