package com.example.zuhauselernen.data.local.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.zuhauselernen.data.local.model.Subject
import com.examples.zuhauselernen.R
import com.examples.zuhauselernen.databinding.SubjectListBinding


class SubjectAdapter(
    private val context: Context,
    private val dataset: List<Subject>,

    private val onSubjectChecked: (Subject, Boolean) -> Unit
) : RecyclerView.Adapter<SubjectAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(val binding: SubjectListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SubjectAdapter.ItemViewHolder {
        val binding = SubjectListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubjectAdapter.ItemViewHolder, position: Int) {
        val subject = dataset[position]

        holder.binding.cbSubject.isChecked = subject.isChecked
        holder.binding.tvSubjectTitel.text=context.getText(subject.subjectName.toInt())
        holder.binding.ivSubjectImage.setImageResource(subject.subjectImage)


        val layoutParams = holder.binding.ivSubjectImage.layoutParams
        val originalWidth = layoutParams.width
        val originalHeight = layoutParams.height
        val checkedColor = ContextCompat.getColor(context, R.color.buttons)
        val uncheckedColor = ContextCompat.getColor(context, R.color.white)
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),
                intArrayOf(-android.R.attr.state_checked)
            ),
            intArrayOf(checkedColor, uncheckedColor)
        )
        holder.binding.cbSubject.buttonTintList = colorStateList

        holder.binding.cbSubject.setOnCheckedChangeListener { _, isChecked ->
            subject.isChecked = isChecked
            onSubjectChecked(subject, isChecked)
            if (isChecked) {
                Toast.makeText(context, "Das Fach ${context.getString(subject.subjectName.toInt())} wurde hinzugefügt.", Toast.LENGTH_SHORT).show()

            } else {

                Toast.makeText(context, "Das Fach ${context.getString(subject.subjectName.toInt())} wurde entfernt.", Toast.LENGTH_SHORT).show()

            }
            val newWidth = if (isChecked) 185 else originalWidth
            val newHeight = if (isChecked) 185 else originalHeight
            layoutParams.width = newWidth
            layoutParams.height = newHeight
            holder.binding.ivSubjectImage.layoutParams = layoutParams
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

}