package com.example.zuhauselernen.data.local.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zuhauselernen.data.local.model.Subject
import com.examples.zuhauselernen.databinding.UserSubjectListBinding

class UserSubjectsAdapter (
    private val context: Context,
    private val dataset: List<Subject>,
    private var onSubjectClickListener: (Subject) -> Unit
) : RecyclerView.Adapter<UserSubjectsAdapter.ItemViewHolder>() {
    inner class ItemViewHolder(val binding: UserSubjectListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val subject = dataset[position]
                    onSubjectClickListener.invoke(subject)
                }
            }
        }
    }



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserSubjectsAdapter.ItemViewHolder {
        val binding = UserSubjectListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val subject = dataset[position]
        holder.binding.tvSubjectTitelUserSubjects.text = context.getText(subject.subjectName.toInt())
        holder.binding.ivUserSubjectsImage.setImageResource(subject.subjectImage)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
    fun setOnSubjectClickListener(listener: (Subject) -> Unit) {
        onSubjectClickListener = listener
    }
}