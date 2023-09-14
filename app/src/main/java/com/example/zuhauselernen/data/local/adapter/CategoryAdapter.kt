package com.example.zuhauselernen.data.local.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zuhauselernen.data.local.model.Category
import com.examples.zuhauselernen.databinding.CategoryListBinding

class CategoryAdapter(
    private val context: Context,
    private val dataset: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.ItemViewHolder>() {

    class ItemViewHolder(val binding: CategoryListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            CategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val category = dataset[position]
        holder.binding.tvCategoryTitel.text = context.getText(category.categoryName.toInt())
        holder.binding.tvCategoryExplanation.text = context.getText(category.explanation.toInt())
        holder.binding.ivCategoryImage.setImageResource(category.categoryImage)

    }

}